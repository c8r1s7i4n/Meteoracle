# Could be improved via Multi-Stage Build Approach
FROM amazoncorretto:25-jdk

# Install necessary utilities for "groupadd"
RUN yum install -y shadow-utils

# Create group and user
RUN groupadd omnipons && \
    useradd -m -g omnipons meteoracle_user

# Switch to the custom user
USER meteoracle_user
WORKDIR /meteoracle
COPY build/libs/meteoracle-0.1.0-SNAPSHOT.war ./meteoracle.war
EXPOSE 80
ENTRYPOINT ["java","-jar","meteoracle.war"]

# !!! Remember, if you don’t set a USER in your Dockerfile, the user will default to root. Always explicitly set a user, even if it’s just to make it clear who the container will run as. !!!
# https://www.docker.com/blog/understanding-the-docker-user-instruction/
