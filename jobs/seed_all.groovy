folder('project-seeds') {
	displayName('Project Seeds')
	description('Folder for all seeds')
}


job('Seed1 test') {
  
  concurrentBuild(true)

  logRotator {
    daysToKeep(1)
    numToKeep(5)
    artifactNumToKeep(1)
  }

  parameters {
	stringParam('proj','none')
	nodeLabelParam('node','hpctest')
  }

  wrappers {
	  colorizeOutput()
	  timestamps()
  }

  displayName('Seed1 Job Example')

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
