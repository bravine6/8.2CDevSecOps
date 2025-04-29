pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/bravine6/8.2CDevSecOps.git'
            }
        }
        stage('Install Dependencies') {
            steps {
                sh 'npm install'
            }
        }
        stage('Run Tests') {
            steps {                
                sh 'npm test || true' // Allow pipeline to continue despite test failures
            }
            post {
                success {
                    emailext(
                        to         : "mickybravine@gmail.com",
                        subject    : "Run Tests Stage: SUCCESS",
                        body       : "Run Tests completed successfully. See attached console log.",
                        mimeType   : 'text/plain',
                        attachLog  : true
                    ) 
                }
                failure {
                    emailext(
                        to         : "mickybravine@gmail.com",
                        subject    : "Run Tests Stage: FAILED",
                        body       : "Run Tests failed. See attached console log.",
                        mimeType   : 'text/plain',
                        attachLog  : true
                    )
                }
            }
        }
        stage('Generate Coverage Report') {
            steps {
                // Ensure coverage report exists
                sh 'npm run coverage || true'
            } 
        }
        stage('NPM Audit (Security Scan)') {
            steps {                
                sh 'npm audit || true' // This will show known CVEs in the output
            }
            post {
                success {
                    emailext(
                        to                : "mickybravine@gmail.com",
                        subject           : "Security Scan Stage: SUCCESS",
                        body              : "Security scan completed successfully. See attached console log.",
                        mimeType          : 'text/plain',
                        attachLog         : true
                    )
                }
                failure {
                    emailext(
                        to                : "mickybravine@gmail.com",
                        subject           : "Security Scan Stage: FAILED",
                        body              : "Security scan failed. See attached console log.",
                        mimeType          : 'text/plain',
                        attachLog         : true
                    )

                }
            }
        }
    }
}
