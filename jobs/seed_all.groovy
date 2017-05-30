folder('project-seeds') {
	displayName('Project Seeds')
	description('Folder for all seeds')
	concurrentBuild()
}

logRotator(14,10,10,10)

job('Seed1 test') {

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
