pipeline {
    agent any

    environment {
        PATH = "/opt/homebrew/bin:/usr/local/bin:${env.PATH}"
        JAVA_HOME = "/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home"
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo 'Kod çekiliyor...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Build ediliyor...'
                dir('Not-App') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Unit Tests') {
            steps {
                dir('Not-App') {
                    sh 'mvn test'
                }
            }
            post {
                always {
                    dir('Not-App') {
                        junit '**/target/surefire-reports/*.xml'
                    }
                }
            }
        }

        stage('Integration Tests') {
            steps {
                dir('Not-App') {
                    sh 'mvn verify'
                }
            }
            post {
                always {
                    dir('Not-App') {
                        junit '**/target/failsafe-reports/*.xml'
                    }
                }
            }
        }

        /* =========================
           DOCKER COMPOSE
        ========================= */
        stage('Run System in Docker') {
            steps {
                sh '''
                    set -e
                    echo "== Docker Compose CLEANUP =="
                    docker compose down || true

                    echo "== Docker Compose BUILD & UP =="
                    docker compose up -d --build

                    echo "== Docker Compose STATUS =="
                    docker compose ps
                '''
            }
        }

        /* =========================
           BACKEND READY
        ========================= */
        stage('Wait for Backend') {
            steps {
                sh '''
                    echo "Waiting for backend (actuator/health)..."
                    for i in {1..30}; do
                      if curl -s http://localhost:8085/actuator/health | grep -q '"status":"UP"'; then
                        echo "Backend is UP"
                        exit 0
                      fi
                      sleep 2
                    done
                    echo "Backend NOT ready"
                    exit 1
                '''
            }
        }

        /* =========================
           FRONTEND READY
        ========================= */
        stage('Wait for Frontend') {
            steps {
                sh '''
                    echo "Waiting for frontend (React)..."
                    for i in {1..30}; do
                      if curl -s http://localhost | grep -q "<div id=\\"root\\""; then
                        echo "Frontend is READY"
                        exit 0
                      fi
                      sleep 2
                    done
                    echo "Frontend NOT ready"
                    exit 1
                '''
            }
        }

        /* =========================
           UI TESTS
        ========================= */
        stage('UI Test: Register Success') {
            steps {
                dir('Not-App') {
                    sh 'mvn test -Pui-tests -Dtest=RegisterUITest#shouldRegisterUser'
                }
            }
        }

        stage('UI Test: Login Fail') {
            steps {
                dir('Not-App') {
                    sh 'mvn test -Pui-tests -Dtest=LoginWrongUITest#shouldRegisterUserAndThenFailLogin'
                }
            }
        }

        stage('UI Test: Login Success') {
            steps {
                dir('Not-App') {
                    sh 'mvn test -Pui-tests -Dtest=LoginSuccessUITest#shouldRegisterAndLoginUserSuccessfully'
                }
            }
        }

        stage('UI Test: Profil Update') {
            steps {
                dir('Not-App') {
                    sh 'mvn test -Pui-tests -Dtest=RegisterLoginProfileUpdateUITest#registerLoginAndUpdateProfile'
                }
            }
        }

        stage('UI Test: Ders Note Add') {
            steps {
                dir('Not-App') {
                    sh 'mvn test -Pui-tests -Dtest=DersNotuAddUITest#registerLoginAndAddDersNotu'
                }
            }
        }

        stage('UI Test: Ders Note Add & Check') {
            steps {
                dir('Not-App') {
                    sh 'mvn test -Pui-tests -Dtest=DersNotuAddAndCheckUITest#registerLoginAddDersNotuAndCheckNotlarimThenLogout'
                }
            }
        }

        stage('UI Test: Ders Add') {
            steps {
                dir('Not-App') {
                    sh 'mvn test -Pui-tests -Dtest=DersAddUITest#registerLoginAddDersAndLogout'
                }
            }
        }

        stage('UI Test: Begen Add') {
            steps {
                dir('Not-App') {
                    sh 'mvn test -Pui-tests -Dtest=BegenAddUITest#loginAndLikeAndUnlikeDersNot'
                }
            }
        }

        stage('UI Test: Begen List') {
            steps {
                dir('Not-App') {
                    sh 'mvn test -Pui-tests -Dtest=BegenListUITest#registerLoginLikeAndGoToFavorilerimAndLogout'
                }
            }
        }
    }
}
