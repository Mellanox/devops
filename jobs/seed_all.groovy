//folder('project-seeds') {
//	displayName('Project Seeds')
//	description('Folder for all seeds')
//}

def defaultEmail = "miked@mellanox.com"
def projName = "Seed1 test"
def jobName = "${projName}-seed-job"
def viewRegex = "${projName}-.*"


listView(projName) {
	   description("Seed job for project ${projName}")
            columns {
                status()
                weather()
                name()
                lastSuccess()
                lastFailure()
                lastDuration()
                buildButton()
            }
            filterBuildQueue()
            filterExecutors()
            jobs {
               regex(/(?i)(${viewRegex})/)
            }
}


job(jobName) {
  
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
		name('origin')
	        github('Mellanox/devops','ssh')
		refspec('+refs/pull/*:refs/remotes/origin/pr/*')
	        credentials('549927eb-7f38-4a8f-997a-81dd63605782')
	}
	branch('${sha1}')
	extensions {
	   cleanAfterCheckout()
           wipeOutWorkspace()
	   cloneOptions {
	     shallow(true)
	     timeout(20)
	   }
	   mergeOptions {
	     remote('origin')
	     branch('master')
	   }
	   submoduleOptions {
	     recursive(true)
	   }
        }
    }
  }


  triggers {
      githubPullRequest {
          admins(['mellanox-hpc','Mellanox','hcoll-team','mellanox-github'])
          orgWhitelist(['mellanox-hpc', 'Mellanox'])
          cron('H/5 * * * *')
          triggerPhrase('bot:retest')
          permitAll()
	  autoCloseFailedPullRequests(false)
	  displayBuildErrorsOnDownstreamBuilds()
          allowMembersOfWhitelistedOrgsAsAdmin()
          onlyTriggerPhrase(false)
	  useGitHubHooks(false)
          extensions {
              commitStatus {
                  context('MellanoxLab')
                  completedStatus('SUCCESS', 'Test PASSed. See ${BUILD_URL} for details (Mellanox internal link).')
                  completedStatus('FAILURE', 'Test FAILed. See ${BUILD_URL} for details (Mellanox internal link).')
                  completedStatus('ERROR',   'Test FAILed (errors).  See ${BUILD_URL} for details. (Mellanox internal link).')
                  completedStatus('PENDING', 'Test still in progress...')
              }
          }
      }
  }

  steps {
    shell('echo START on $(hostname)')
    shell('env')
    dsl {
      external('jobs/*.groovy')  
      removeAction('DELETE')
      removeViewAction("DELETE")
    }
  }

  recipients = defaultEmail
  publishers {
    githubCommitNotifier()
    chucknorris()
    extendedEmail {
       recipientList(recipients)
       triggers {
          failure {
             sendTo {
                recipientList()
             }
           }
           fixed {
              sendTo {
                recipientList()
              }
            }
        }
    }
  }
}
