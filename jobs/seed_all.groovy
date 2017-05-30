folder('project-seeds') {
	displayName('Project Seeds')
	description('Folder for all seeds')
}


job('Seed1 test') {
  
  displayName('Seed1 Job Example')
  concurrentBuild(true)

  logRotator {
    daysToKeep(1)
    numToKeep(5)
    artifactNumToKeep(1)
  }

  parameters {
	stringParam('proj','none')
	labelParam('my_label') {
		defaultValue('hpc-*')
		allNodes('allCases','AllNodeEligibility')
		description('Run on nodes')
	}
//	nodeParam('node') {
//		description('Select test nodes')
//		defaultNodes(['hpc-test-node'])
//		allowedNodes(['hpc-test-node','hpc-arm-04'])
//		trigger('allCases')
//		eligibility('IgnoreOfflineNodeEligibility')
//	}

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
    dsl {
      external('jobs/*.groovy')  
      // default behavior
      // removeAction('IGNORE')      
      removeAction('DELETE')
    }
  }
}
