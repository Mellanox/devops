folder('project-seeds') {
	displayName('Project Seeds')
	description('Folder for all seeds')
}

job('Seed All') {

  displayName('Seed DSK Example')

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
