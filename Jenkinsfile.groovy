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
                    mail to: "mickybravine@gmail.com",
                    subject: "Run Tests Stage: SUCCESS",
                    body: "Run Tests has been successfully finished."
                    attachLog: true
                }
                failure {
                    mail to: "mickybravine@gmail.com",
                    subject: "Run Tests Stage: FAILED",
                    body: "Run Tests has failed."
                    attachLog: true
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
                    mail to: "mickybravine@gmail.com",
                    subject: "Security Scan: SUCCESS",
                    body: "Security scan has been successfully finished"
                    attachLog: true
                }
                failure {
                    mail to: "mickybravine@gmail.com",
                    subject: "Security scan Stage: FAILED",
                    body: "NPM Audit has failed."
                    attachLog: true
                }
            }
        }
    }
}
