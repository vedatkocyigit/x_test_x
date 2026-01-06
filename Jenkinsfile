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
                echo 'Birim testler...'
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
                echo 'Entegrasyon testleri...'
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

        stage('Run System in Docker') {
    steps {
        echo 'Docker Compose ile sistem ayağa kaldırılıyor...'
        sh '''
            set -e

            echo "== Docker Compose CLEANUP (if exists) =="
            docker compose -f docker-compose.yml down || true

            echo "== Docker Compose BUILD & UP =="
            docker compose -f docker-compose.yml up -d --build

            echo "== Docker Compose STATUS =="
            docker compose -f docker-compose.yml ps
        '''
    }
}

        stage('Wait for System') {
            steps {
                echo 'Sistem ayaga kalkıyor, 10 saniye bekleniyor...'
                sleep time: 10, unit: 'SECONDS'
            }
        }

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
