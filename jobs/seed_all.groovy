folder('project-seeds') {
	displayName('Project Seeds')
	description('Folder for all seeds')
}

logRotator(14,10,10,10)

job('Seed1 test') {
  
  concurrentBuild(true)
  parameters {
	  stringParam('proj','none')
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
