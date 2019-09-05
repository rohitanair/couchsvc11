def readProperties()
{

	def properties_file_path = "${workspace}" + "@script/properties.yml"
	def property = readYaml file: properties_file_path
	env.APP_NAME = property.APP_NAME
    env.MS_NAME = property.MS_NAME
    env.BRANCH = property.BRANCH
    env.GIT_SOURCE_URL = property.GIT_SOURCE_URL
    env.SCR_CREDENTIALS = property.SCR_CREDENTIALS
	env.GIT_CREDENTIALS = property.GIT_CREDENTIALS
    env.SONAR_HOST_URL = property.SONAR_HOST_URL
    env.CODE_QUALITY = property.CODE_QUALITY
    env.UNIT_TESTING = property.UNIT_TESTING
    env.CODE_COVERAGE = property.CODE_COVERAGE
    env.FUNCTIONAL_TESTING = property.FUNCTIONAL_TESTING
    env.SECURITY_TESTING = property.SECURITY_TESTING
	env.PERFORMANCE_TESTING = property.PERFORMANCE_TESTING
	env.TESTING = property.TESTING
	env.QA = property.QA
	env.PT = property.PT
	env.DOCKER_REGISTRY = property.DOCKER_REGISTRY
	env.mailrecipient = property.mailrecipient
	env.USER = property.USER
	env.REPO = property.REPO
	env.PR_TITLE = property.PR_TITLE
	env.PR_MSG = property.PR_MSG
	env.HEAD_BRANCH = property.HEAD_BRANCH
	env.BASE_BRANCH = property.BASE_BRANCH
	env.EXPECTED_COVERAGE = property.EXPECTED_COVERAGE
	
    
}



def buildApp(projectName,msName){
openshift.withCluster() {
        openshift.withProject(projectName) {
            def bcSelector = openshift.selector( "bc", msName) 
            def bcExists = bcSelector.exists()
            if (!bcExists) {
                openshift.newBuild("https://github.com/Vageesha17/projsvc","--strategy=docker")
                sh 'sleep 400'               
            } else {
                sh 'echo build config already exists in development environment,starting existing build'  
                openshift.startBuild(msName,"--wait")                
            }
        }
}
}
def FAILED_STAGE
podTemplate(cloud:'openshift',label: 'selenium', 
  containers: [
    containerTemplate(
      name: 'jnlp',
      image: 'cloudbees/jnlp-slave-with-java-build-tools',
      alwaysPullImage: true,
      args: '${computer.jnlpmac} ${computer.name}'
    ),
	 containerTemplate(
      name: 'docker',
      image: 'manya97/jenkins_tryout',
      alwaysPullImage: true,
      args: '${computer.jnlpmac} ${computer.name}',
      ttyEnabled: true
    )],volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock'),hostPathVolume(hostPath: '/etc/docker/daemon.json', mountPath: '/etc/docker/daemon.json')] )
{
node 
{
   def MAVEN_HOME = tool "Maven_HOME"
   def JAVA_HOME = tool "JAVA_HOME"
   env.PATH="${env.PATH}:${MAVEN_HOME}/bin:${JAVA_HOME}/bin"
	try{
   stage('Checkout')
   {
       FAILED_STAGE=env.STAGE_NAME
       readProperties()
       checkout([$class: 'GitSCM', branches: [[name: "*/${BRANCH}"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: "${SCR_CREDENTIALS}", url: "${GIT_SOURCE_URL}"]]])
       sh 'oc set image --local=true -f Orchestration/deployment.yaml ${MS_NAME}=${DOCKER_REGISTRY}/smartfan-dev-apps/${MS_NAME}:smartfan-dev-apps --dry-run -o yaml >> Orchestration/deployment-smartfan-dev.yaml'

   }

   stage('Initial Setup')
   {
		FAILED_STAGE=env.STAGE_NAME
       sh 'mvn -s Maven/setting clean compile'
   }
   if(env.UNIT_TESTING == 'True')
   {
        stage('Unit Testing')
        {
			FAILED_STAGE=env.STAGE_NAME
              sh 'mvn -s Maven/setting test'
        }
   }
  
   if(env.CODE_QUALITY == 'True')
   {
        stage('Code Quality Analysis')
        {
			  FAILED_STAGE=env.STAGE_NAME
              sh 'mvn -s Maven/setting sonar:sonar -Dsonar.host.url="${SONAR_HOST_URL}"'
        }
   }
    
    if(env.CODE_COVERAGE == 'True')
   {
      stage('Code Coverage')
   	{
		FAILED_STAGE=env.STAGE_NAME
        sh 'mvn -s Maven/setting package -Djacoco.percentage.instruction=${EXPECTED_COVERAGE}'
       
   	}
   }
  if(env.SECURITY_TESTING == 'True')
  {
      stage('Security Testing')
      {
			FAILED_STAGE=env.STAGE_NAME
			sh 'mvn -s Maven/setting findbugs:findbugs'
      }	
  }
   
       stage('Pull Request Generation')
   {
        
        withCredentials([usernamePassword(credentialsId: "${SCR_CREDENTIALS}", passwordVariable: 'password', usernameVariable: 'username')]) 
        {
             sh """
        curl -k -X POST -u $username:$password https://api.github.com/repos/${USER}/${REPO}/pulls \
        -d  "{
            \\"title\\": \\"merging master\\",
            \\"body\\": \\"${PR_MSG}\\",
            \\"head\\": \\"${USER}:${HEAD_BRANCH}\\",
            \\"base\\": \\"${BASE_BRANCH}\\"
        }\""""
	    }
	    //emailext body: "Pull Request has been raised. You can review at https://github.com/${USER}/${REPO}/pulls (Please open this in chrome) ", subject: "Pull Request Generated", to: '${mailrecipient}'
    }

   
   
   
	}
	catch(e){
		echo "Pipeline has failed"
		emailext body: "${env.BUILD_URL} has result ${currentBuild.result} at stage ${FAILED_STAGE} with error" + e.toString(), subject: "Failure of pipeline: ${currentBuild.fullDisplayName}", to: '${mailrecipent}'
		throw e
	}
}
}	