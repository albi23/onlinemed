export class SideBarIconMapper {

  private static mapper: Map<string, string> = new Map<string, string>(
    [
    ['user-management', 'fas fa-users-cog'],
    ['drug-equivalents', 'fas fa-pills'],
    ['profile', 'fas fa-user-circle'],
    ['doctors-profile', 'fas fa-hospital-user'],
    ['notifications', 'far fa-bell'],
    ['forum', 'far fa-comments'],
  ]);

  private static countryIconMapper: Map<string, string> = new Map<string, string>(
    [
      ['en_GB', '🇬🇧'],
      ['pl_PL', '🇵🇱'],
    ]);

  static getCssIcon(name: string): string {
    return this.mapper.get(name) || '';
  }

  static getFlagIcon(languageName: string): string {
    return this.countryIconMapper.get(languageName) || languageName;
  }
}
