pipeline {
  agent any
  stages {
    stage('Begin') {
      steps {
        input 'Compile the code?'
      }
    }
    stage('Compile') {
      steps {
        sh '''echo \'Compiling Code...\'
'''
        sh 'javac Muser.java'
      }
    }
    stage('Run') {
      steps {
        sh 'echo \'Running the JavaFX Application...\''
        sh 'java Muser'
      }
    }
  }
}