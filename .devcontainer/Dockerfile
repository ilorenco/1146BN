FROM mcr.microsoft.com/devcontainers/java:17

# Cria o diretório da aplicação
WORKDIR /workspace/auth-service

# Copia os arquivos do projeto
COPY auth-service/.mvn .mvn
COPY auth-service/mvnw .
COPY auth-service/pom.xml .

# Baixa dependências offline
RUN chmod +x mvnw && ./mvnw dependency:go-offline

# Copia o restante da aplicação
COPY auth-service .

# Define o diretório de trabalho final
WORKDIR /workspace/auth-service
