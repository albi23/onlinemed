package com.onlinemed.testdata;

import com.onlinemed.functionalUtils.FunctionalUtils;
import com.onlinemed.model.*;
import com.onlinemed.model.enums.NotificationType;
import com.onlinemed.model.enums.RoleType;
import com.onlinemed.servises.api.*;
import com.onlinemed.servises.impl.SQLFileQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.onlinemed.model.SystemFunctionalities.*;

/**
 * Class responsible for create test data during development.
 */
@Component
public class MockData implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(MockData.class);

    @Value("${test.data}")
    private String runTestData;

    private final PersonService personService;

    private final ForumCategoryService forumCategoryService;

    private final ForumTopicService forumTopicService;

    private final ForumPostService forumPostService;

    private final SQLFileQueryService sqlFileQueryService;

    private final SecurityService securityService;


    private Map<String, Functionality> functionalityMap;
    private String[] doctorTypes = {
            "Internal Medicine Physician", "Pediatrician", "Obstetrician", "Psychiatrist", "Cardiologist",
            "Endocrinologist", "Gastroenterologist", "Nephrologist", "Ophthalmologist", "Otolaryngologist",
            "Pulmonologist", "Neurologist", "Physician Executive", "Radiologist", "Anesthesiologist", "Oncologist"
     };

    public MockData(PersonService personService,
                    ForumCategoryService forumCategoryService,
                    ForumTopicService forumTopicService,
                    ForumPostService forumPostService,
                    SQLFileQueryService sqlFileQueryService,
                    SecurityService securityService) {
        this.personService = personService;
        this.forumCategoryService = forumCategoryService;
        this.forumTopicService = forumTopicService;
        this.forumPostService = forumPostService;
        this.sqlFileQueryService = sqlFileQueryService;
        this.securityService = securityService;
    }

    @Override
    public void run(ApplicationArguments args) {
        this.insertTranslations();
        if (runTestData.equals("true")) {
            logger.info(String.format("[%s] START of creating test data", new Timestamp(new Date().getTime())));
            this.createTestData();
            logger.info(String.format("[%s] END of creating test data", new Timestamp(new Date().getTime())));
        } else {
            logger.info(String.format("[%s] START of insert sql files", new Timestamp(new Date().getTime())));
            this.insertProductionSql();
            logger.info(String.format("[%s] END of insert sql files", new Timestamp(new Date().getTime())));
        }
    }

    private void createTestData() {
        final List<Person> personLists = this.createPersonLists();
        this.assignRolesToPerson(personLists);
        this.assignCommunityInfo(personLists);
        this.assignEvents(personLists);
        this.assignDoctorInfo(personLists);
        this.assignNotificationsToPerson(personLists);
        this.savePersonEntitiesToDb(personLists);
        this.createForumContent(personLists);
    }


    private void savePersonEntitiesToDb(List<Person> personLists) {
        Map<RoleType, Integer> typeOnVersion = new HashMap<>();
        typeOnVersion.put(RoleType.ADMINISTRATOR, 0);
        typeOnVersion.put(RoleType.USER, 0);
        typeOnVersion.put(RoleType.DOCTOR, 0);
        typeOnVersion.put(RoleType.MODERATOR, 0);

        for (Person p : personLists) {
            p.getRoles().forEach(role -> {
                role.setVersion(typeOnVersion.get(role.getRoleType()));
                role.getFunctionalities().forEach(f -> f.setVersion(functionalityMap.get(f.getName()).getVersion()));
            });
            final Person merge = personService.merge(p);
            merge.getRoles().forEach(role -> {
                typeOnVersion.computeIfPresent(role.getRoleType(), (k, v) -> role.getVersion());
                role.getFunctionalities().forEach(f -> functionalityMap.computeIfPresent(f.getName(), (k, v) -> {
                    v.setVersion(f.getVersion());
                    return v;
                }));
            });
        }
    }

    /**
     * list[0] - Admin,
     * list[1] - Moderator
     * list[2] - list[7] - Doctors
     * list[8] - list[17] -Users
     *
     * @return List of Person
     */
    private List<Person> createPersonLists() {
        final List<Person> personDataByReflection = getPersonDataByReflection();
        // set admin password to admin123
        final Person admin = personDataByReflection.get(0);
        admin.setSecurity(new Security(admin, securityService.hashPassword("admin123")));

        // generate test users
        var people = Stream.iterate(1, t -> t + 1).limit(10)
                .map(i -> {
                    final String iter = String.valueOf(i);
                    return createPerson(UUID.randomUUID(), "test-user-name".concat(iter), "test-surname".concat(iter),
                            "TestUser".concat(iter), "test.user".concat(iter).concat("@test.com"), "+48666777888", "TestUser".concat(iter));
                }).toList();
        personDataByReflection.addAll(people);

        return personDataByReflection;
    }

    private List<Person> getPersonDataByReflection() {
        final Field[] fields = TestData.class.getFields();
        final ArrayList<Person> people = new ArrayList<>(7);
        for (int i = 0, j = fields.length - 1; i < j; i += 4) {
            int finalI = i;
            UUID id = FunctionalUtils.optionalSupplierWrapper(() -> (UUID) fields[finalI].get(UUID.class), UUID.randomUUID());
            String username = FunctionalUtils.optionalSupplierWrapper(() -> (String) fields[finalI + 1].get(String.class), "null");
            String name = FunctionalUtils.optionalSupplierWrapper(() -> (String) fields[finalI + 2].get(String.class), "null");
            String surname = FunctionalUtils.optionalSupplierWrapper(() -> (String) fields[finalI + 3].get(String.class), "null");
            people.add(createPerson(id, name, surname, username, name.concat(surname).concat("@niepodam.pl"), "+48696878959", username));
        }
        return people;
    }

    public Person createPerson(UUID id, String name, String surname, String userName,
                               String email, String phoneNumber, String password) {
        final Person person = new Person(id, name, surname, userName, email, phoneNumber);
        person.setSecurity(new Security(person, securityService.hashPassword(password)));
        return person;
    }

    private void assignRolesToPerson(List<Person> people) {
        initSystemFunctionalities();
        Role ADMINISTRATOR_ROLE = new Role(RoleType.ADMINISTRATOR, getFunctionalitiesByRole(RoleType.ADMINISTRATOR));
        Role MODERATOR_ROLE = new Role(RoleType.MODERATOR, getFunctionalitiesByRole(RoleType.MODERATOR));
        Role DOCTOR_ROLE = new Role(RoleType.DOCTOR, getFunctionalitiesByRole(RoleType.DOCTOR));
        Role USER_ROLE = new Role(RoleType.USER, getFunctionalitiesByRole(RoleType.USER));
        people.get(0).setRoles(List.of(ADMINISTRATOR_ROLE, MODERATOR_ROLE, DOCTOR_ROLE, USER_ROLE));
        people.get(1).setRoles(List.of(MODERATOR_ROLE));
        Stream.of(people.subList(2, 8)).flatMap(Collection::stream).forEach(p -> p.setRoles(List.of(DOCTOR_ROLE)));
        Stream.of(people.subList(8, people.size())).flatMap(Collection::stream).forEach(p -> p.setRoles(List.of(USER_ROLE)));
    }

    private void initSystemFunctionalities() {
        final Map<String, Functionality> nameOnFunctionality = new HashMap<>();
        for (Field field : SystemFunctionalities.class.getFields()) {
            String fieldValueName = FunctionalUtils.throwingSupplierWrapper(() -> (String) field.get(String.class),
                    IllegalAccessException.class);
            nameOnFunctionality.put(fieldValueName, new Functionality(fieldValueName));
        }
        functionalityMap = nameOnFunctionality;
    }

    private List<Functionality> getFunctionalitiesByRole(RoleType roleType) {

        return switch (roleType) {
            case ADMINISTRATOR -> List.of(get(USER_MANAGEMENT), get(DRUG_EQUIVALENTS), get(PROFILE),
                    get(DOCTORS_PROFILE), get(NOTIFICATIONS), get(FORUM));
            case DOCTOR -> List.of(get(DRUG_EQUIVALENTS), get(PROFILE), get(DOCTORS_PROFILE), get(FORUM), get(NOTIFICATIONS));
            case USER, MODERATOR -> List.of(get(PROFILE), get(DOCTORS_PROFILE), get(NOTIFICATIONS), get(FORUM));
        };
    }

    private Functionality get(String SystemFunctionalities) {
        return this.functionalityMap.get(SystemFunctionalities);
    }

    private void assignCommunityInfo(List<Person> personLists) {
        Random rand = new Random(System.currentTimeMillis());
        long offset = Timestamp.valueOf("2020-01-01 00:00:00").getTime();
        long end = Timestamp.valueOf(LocalDateTime.now()).getTime();
        long diff = end - offset + 1;
        personLists.forEach(p -> {
            LocalDateTime registerDate = new Timestamp(offset + (long) (Math.random() * diff)).toLocalDateTime();
            LocalDateTime lastLogin = registerDate.plusDays(5);
            p.setCommunity(new Community(rand.nextInt(100), descriptionText(), registerDate, lastLogin, p));
        });
    }

    private String descriptionText() {
        return " Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc nibh nulla, pretium at posuere quis," +
                " sagittis ac purus. Morbi congue dui eget neque iaculis consectetur. Quisque non risus massa." +
                " Phasellus quis augue eget tellus sagittis dictum. Curabitur pellentesque magna enim, " +
                "vel finibus urna consequat at. In ac neque pretium, varius ex ut, maximus sapien. " +
                "Donec ac dignissim erat. Interdum et malesuada fames ac ante ipsum primis in faucibus. " +
                "Vivamus sagittis libero non metus scelerisque rutrum. Pellentesque et neque molestie, " +
                "commodo nisl nec, aliquet turpis. Nullam efficitur hendrerit mauris, vel placerat ipsum";
    }

    private void assignEvents(List<Person> personLists) {
        Random rand = new Random(System.currentTimeMillis());
        long offset = Timestamp.valueOf(LocalDateTime.now().withHour(8).minusDays(1)).getTime();
        long end = Timestamp.valueOf(LocalDateTime.now().plusDays(5).withHour(20)).getTime();
        long diff = end - offset + 1;
        for (Person person : personLists.subList(0, 8)) {
            List<CalendarEvent> events = Stream.iterate(1, t -> t + 1).limit(rand.nextInt(6) + 4).map(
                    i -> {
                        LocalDateTime startEventTime = new Timestamp(offset + (long) (Math.random() * diff))
                                .toLocalDateTime().withHour(rand.nextInt(10) + 8);
                        LocalDateTime endEventTime = startEventTime.plusMinutes(30);
                        return new CalendarEvent(startEventTime, endEventTime, "Visit" + i, person);
                    }).collect(Collectors.toList());
            person.setCalendarEvents(events);
        }

    }

    private void assignDoctorInfo(List<Person> personLists) {
        Random rand = new Random(System.currentTimeMillis());
        personLists.subList(0, 8).forEach(
                p -> {
                    final DoctorInfo doctorInfoObj = this.createDoctorInfoObj(rand);
                    final List<FacilityLocation> facilityLocations = Stream.iterate(1, t -> t + 1).limit(2).map(t -> {
                        final FacilityLocation facilityLocation = new FacilityLocation("Doctors Clinic nr. " + rand.nextInt(20),
                                "Some Street " + rand.nextInt(100) + ", Some City" + rand.nextInt(100));
                        HashMap<String, String> visitsPriceList = new HashMap<>();
                        Stream.iterate(1, q -> q + 1).limit(6).forEach(i -> visitsPriceList.put("Visit Type " + ((char) ((t + 64))) + "" + i, String.valueOf((rand.nextInt(10) + 1) * 100)));
                        facilityLocation.setVisitsPriceList(visitsPriceList);
                        return facilityLocation;
                    }).collect(Collectors.toList());
                    doctorInfoObj.setFacilityLocations(facilityLocations);
                    p.setDoctorInfo(doctorInfoObj);
                });
        doctorTypes = null; // clear
    }

    private DoctorInfo createDoctorInfoObj(Random rand) {
        return new DoctorInfo("458689325", doctorTypes[rand.nextInt(16)], "Specialization " + rand.nextInt(100));
    }


    private void assignNotificationsToPerson(List<Person> personLists) {
        final Person admin = personLists.get(0);
        final List<Notification> notifications = personLists.subList(1, 8).stream().map(p -> new Notification(p.getId(), admin, p.getName(), p.getSurname()))
                .collect(Collectors.toList());

        final Random rand = new Random(System.currentTimeMillis());
        long offset = Timestamp.valueOf(LocalDateTime.now().withHour(8).plusDays(rand.nextInt(10))).getTime();
        long end = Timestamp.valueOf(LocalDateTime.now().plusDays(5).withHour(20)).getTime();
        long diff = end - offset + 1;

        final NotificationType[] values = NotificationType.values();
        Stream.iterate(2, i -> i + 1).limit(4).forEach(i -> {
            final Notification notification = notifications.get(i);
            final List<FacilityLocation> locations = personLists.get(i).getDoctorInfo().getFacilityLocations();
            notification.setNotificationType(values[i % values.length]);
            final FacilityLocation facilityLocation = locations.get(rand.nextInt(locations.size()));
            final String[] strings = facilityLocation.getVisitsPriceList().keySet().toArray(String[]::new);
            final String visitType = strings[rand.nextInt(strings.length)];
            LocalDateTime visitTime = new Timestamp(offset + (long) (Math.random() * diff))
                    .toLocalDateTime().withHour(rand.nextInt(10) + 8);
            notification.setVisit(new Visit(visitType, facilityLocation.getFacilityName(), visitTime));
        });
        admin.setNotifications(notifications);
    }

    private List<ForumCategory> createForumCategories() {
        final String[] categories = {
                "forum.healthy-eating", "forum.sport-and-health", "forum.proven-sources",
                "forum.hospitals", "forum.health-base", "forum.treatment"
        };
        final String categoryDesc = "forum.discus-about";
        final String[] icons = {
                "fas fa-weight", "fas fa-heartbeat", "fas fa-laptop-medical",
                "far fa-hospital", "fas fa-book-medical", "fab fa-accessible-icon",
        };
        return IntStream.iterate(0, i -> i + 1).limit(icons.length)
                .mapToObj(i -> new ForumCategory(categories[i], icons[i], categoryDesc))
                .collect(Collectors.toList());
    }

    private Stream<ForumTopic> createForumTopicsForCategory(final ForumCategory category, final String creator) {
        final String hasztags = Stream.iterate(1, j -> j + 1).limit(3)
                .map(h -> "hasztag".concat(String.valueOf(h))).collect(Collectors.joining(";"));

        return IntStream.iterate(1, i -> i + 1).limit(3)
                .mapToObj(i -> new ForumTopic("Title " + i, creator, hasztags, category));

    }

    private Stream<ForumPost> createForumPosts(final ForumTopic forumTopic, final Person creator) {
        return IntStream.iterate(1, i -> i + 1).limit(3)
                .mapToObj(i -> {
                    final ForumPost forumPost = new ForumPost(creator, this.getPostContent(), forumTopic);
                    if (i == 1) forumPost.setId(forumTopic.getId());
                    return forumPost;
                });
    }

    private String getPostContent() {
        return " Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc nibh nulla, pretium at posuere quis," +
                " sagittis ac purus. Morbi congue dui eget neque iaculis consectetur. Quisque non risus massa." +
                " Phasellus quis augue eget tellus sagittis dictum. Curabitur pellentesque magna enim, " +
                "vel finibus urna consequat at. In ac neque pretium, varius ex ut, maximus sapien. ";
    }

    private void createForumContent(final List<Person> personList) {
        final List<ForumCategory> forumCategories = this.createForumCategories();

        final Random rand = new Random(System.currentTimeMillis());
        final List<ForumTopic> forumTopics = forumCategories.stream().flatMap(forumCat -> {
            final String creator = personList.get(rand.nextInt(personList.size())).getUserName();
            return this.createForumTopicsForCategory(forumCat, creator);
        }).toList();

        final List<ForumPost> forumPosts = forumTopics.stream().flatMap(topic -> {
            final Person creator = personList.get(rand.nextInt(personList.size()));
            return this.createForumPosts(topic, creator);
        }).collect(Collectors.toList());

        /* 1 topic with bigger amount of post*/
        final ForumTopic forumTopic1 = forumTopics.get(0);
        ForumPost post = new ForumPost(personList.get(rand.nextInt(personList.size())), this.getPostContent(), forumTopic1);
        post.setId(forumTopic1.getId());
        forumPosts.add(post);

        Stream.iterate(1, t -> t + 1).limit(29).forEach(i -> {
            final Person creator = personList.get(rand.nextInt(personList.size()));
            forumPosts.add(new ForumPost(creator, this.getPostContent(), forumTopic1));
        });

        forumCategories.forEach(forumCategory -> this.forumCategoryService.merge(forumCategory));
        forumTopics.forEach(forumTopic -> this.forumTopicService.merge(forumTopic));
        forumPosts.forEach(forumPost -> this.forumPostService.merge(forumPost));
    }

    public void insertTranslations() {
        final String[] scripts = new String[]{
                "sql/languages.sql",
                "sql/translation_modules.sql",
                "sql/static_translations_en.sql",
                "sql/static_translations_pl.sql"
        };
        sqlFileQueryService.executeScriptsSql(scripts);
    }

    private void insertProductionSql() {
        final String[] scripts = new String[]{
                "sql/production/onlinemed_db_public_security.sql",
                "sql/production/onlinemed_db_public_person.sql",
                "sql/production/onlinemed_db_public_role.sql",
                "sql/production/onlinemed_db_public_functionality.sql",
                "sql/production/onlinemed_db_public_person_x_role.sql",
                "sql/production/onlinemed_db_public_role_x_functionality.sql",
                "sql/production/onlinemed_db_public_doctor_info.sql",
                "sql/production/onlinemed_db_public_facility_location.sql",
                "sql/production/onlinemed_db_public_person_x_doctor_info.sql",
                "sql/production/onlinemed_db_public_values.sql",
                "sql/production/onlinemed_db_public_community.sql"
        };
        sqlFileQueryService.executeScriptsSql(scripts);
    }
}