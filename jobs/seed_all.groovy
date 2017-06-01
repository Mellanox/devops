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
	stringParam('sha1', 'origin/master','git ref to build')
	nodeParam('node') {
		description('Select test nodes')
		defaultNodes(['hpc-test-node'])
		allowedNodes(['hpc-test-node','hpc-arm-04'])
		trigger('allowMultiSelectionForConcurrentBuilds')
		eligibility('IgnoreOfflineNodeEligibility')
	}

  }

  wrappers {
	  colorizeOutput()
	  timestamps()
  }


  scm {
    git {
	remote {
	        github('miked-mellanox/devops','ssh')
                refspec('+refs/pull/*:refs/remotes/origin/pr/*')
	        credentials('549927eb-7f38-4a8f-997a-81dd63605782')
	}
	branch('${sha1}')
	extensions {
           wipeOutWorkspace()
        }
    }
  }


  triggers {
      githubPullRequest {
          admins(['mellanox-hpc','Mellanox','hcoll-team'])
          orgWhitelist(['mellanox-hpc', 'Mellanox'])
          cron('H/2 * * * *')
          triggerPhrase('bot:retest')
          onlyTriggerPhrase()
          permitAll()
	  autoCloseFailedPullRequests()
	  displayBuildErrorsOnDownstreamBuilds()
          allowMembersOfWhitelistedOrgsAsAdmin()
	  useGitHubHooks()
          //extensions {
          //    commitStatus {
          //        context('MellanoxLab')
          //        completedStatus('SUCCESS', 'Test PASSed. See ${BUILD_URL} for details (Mellanox internal link).')
          //        completedStatus('FAILURE', 'Test FAILed. See ${BUILD_URL} for details (Mellanox internal link).')
          //        completedStatus('ERROR',   'Test FAILed (errors).  See ${BUILD_URL} for details. (Mellanox internal link).')
          //        completedStatus('PENDING', 'Test still in progress...')
          //    }
          //}
      }
  }

  steps {
    shell('echo START on $(hostname)')
    shell('env')
    dsl {
      external('jobs/*.groovy')  
      // default behavior
      // removeAction('IGNORE')      
      removeAction('DELETE')
    }
  }

  publishers {
    githubCommitNotifier()
  }
}
