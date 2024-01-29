FROM nginx:stable-alpine3.17-perl
RUN echo 'Coping sources from ${pwd} to docker image dir/dist/onlinemed-web-ui'
WORKDIR /dist/onlinemed-web-ui
COPY ./onlinemed-web-ui/dist/onlinemed-web-ui /usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
RUN echo 'Deploying onlinemed-ui finished'