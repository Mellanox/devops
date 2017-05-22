job('Seed All') {
  scm {
    git ('https://github.com/miked-mellanox/devops.git')
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
