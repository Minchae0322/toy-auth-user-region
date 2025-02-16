pipeline {
    agent any

    environment {
        BACKUP_DIR = "/backup/jenkins_builds/${JOB_NAME}" // 백업 기본 디렉토리
        PREVIOUS_VERSION_FILE = "/backup/jenkins_builds/${JOB_NAME}/last_build_version.txt" // 이전 빌드 기록 파일
        DOCKER_IMAGE = "auth-image:${BUILD_NUMBER}" // Docker 이미지 태그
        DOCKER_NETWORK = "app-network" // Docker 네트워크 이름
        ENV_FILE = "toy.env" // 환경 변수 파일명
    }

    stages {
        stage('Prepare Version') {
            steps {
                script {
                    def timestamp = new Date().format("yyyyMMdd-HHmmss", TimeZone.getTimeZone('UTC'))
                    def commitHash = env.GIT_COMMIT ? env.GIT_COMMIT.take(7) : 'manual'
                    env.BUILD_VERSION = "${BUILD_NUMBER}-${commitHash}-${timestamp}"
                    echo "Current Build Version: ${env.BUILD_VERSION}"

                    if (fileExists(env.PREVIOUS_VERSION_FILE)) {
                        env.PREVIOUS_BUILD_VERSION = readFile(env.PREVIOUS_VERSION_FILE).trim()
                        echo "Previous Build Version: ${env.PREVIOUS_BUILD_VERSION}"
                    } else {
                        echo "No previous build version found."
                    }
                }
            }
        }

        stage('Load Environment Variables') {
            steps {
                script {
                    if (fileExists("toy.env")) {
                        def envVars = readFile("toy.env").trim().split("\n")
                        def envList = []
                        envVars.each { line ->
                            def keyValue = line.tokenize('=')
                            if (keyValue.size() == 2) {
                                def key = keyValue[0].trim()
                                def value = keyValue[1].trim()
                                envList.add("${key}=${value}")
                                echo "Loaded ENV: ${key}"
                                env[key] = value // 전역 환경 변수 설정
                            }
                        }
                        echo "✅ Environment variables successfully loaded"
                    } else {
                        error("🚨 Error: toy.env file not found!")
                    }
                }
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean build'
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    def jarFile = sh(script: "ls -1 build/libs/*.jar | grep -v plain | head -n 1", returnStdout: true).trim()
                    echo "Using JAR File: ${jarFile}"
                    sh """
                    docker build -t ${DOCKER_IMAGE} --build-arg JAR_FILE=${jarFile} .
                    """
                }
            }
        }

        stage('Docker Run') {
            steps {
                script {
                    sh '''
                    if [ ! -f "toy.env" ]; then
                        echo "🚨 Error: toy.env file not found!"
                        exit 1
                    fi
                    '''

                    sh '''
                    if ! docker network ls | grep -q ${DOCKER_NETWORK}; then
                        docker network create ${DOCKER_NETWORK}
                    fi
                    '''

                    sh '''
                    docker stop auth-container || true
                    docker rm auth-container || true
                    docker rmi $(docker images -q auth-image) || true
                    docker run -d --name auth-container --network ${DOCKER_NETWORK} -p 8081:8081 --env-file toy.env ${DOCKER_IMAGE}
                    '''
                }
            }
        }
    }

    post {
        failure {
            script {
                if (env.PREVIOUS_BUILD_VERSION) {
                    echo "Restoring previous build version: ${env.PREVIOUS_BUILD_VERSION}"
                    sh '''
                    if [ -d "${BACKUP_DIR}/${PREVIOUS_BUILD_VERSION}" ]; then
                        cp -r ${BACKUP_DIR}/${PREVIOUS_BUILD_VERSION}/* build/libs/
                        echo "Restored previous build artifacts from ${PREVIOUS_BUILD_VERSION}"
                    else
                        echo "No backup available for previous build version: ${PREVIOUS_BUILD_VERSION}"
                    fi
                    '''
                } else {
                    echo "No previous build version available to restore."
                }
            }
        }

        success {
            script {
                echo "Build succeeded with version: ${env.BUILD_VERSION}"
                sh '''
                mkdir -p ${BACKUP_DIR}/${BUILD_VERSION}
                cp build/libs/*.jar ${BACKUP_DIR}/${BUILD_VERSION}/
                echo "${BUILD_VERSION}" > ${PREVIOUS_VERSION_FILE}
                echo "Current build artifacts saved with version: ${BUILD_VERSION}"
                '''
            }
        }
    }
}
