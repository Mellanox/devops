//folder('project-seeds') {
//	displayName('Project Seeds')
//	description('Folder for all seeds')
//}


job('Seed1 test') {
  
  concurrentBuild(true)

  logRotator {
    daysToKeep(1)
    numToKeep(5)
    artifactNumToKeep(1)
  }

  parameters {
	nodeParam('node') {
		description('Select test nodes')
		defaultNodes(['hpc-test-node'])
		allowedNodes(['hpc-test-node','hpc-arm-04'])
		trigger('allCases')
		eligibility('IgnoreOfflineNodeEligibility')
	}

  }

  wrappers {
	  colorizeOutput()
	  timestamps()
  }


  scm {
    github('miked-mellanox/devops.git', 'master')
  }

  triggers {
	  githubPush()
  }

  steps {
    shell('echo START')
    dsl {
      external('jobs/*.groovy')  
      // default behavior
      // removeAction('IGNORE')      
      removeAction('DELETE')
    }
  }
}
