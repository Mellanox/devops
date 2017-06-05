def run_workspace_script(filename) {
  def code = new File(build.workspace.child(filename).toString()).text
  def code_as_closure = "{->${code}}"
  def closure = evaluate(code_as_closure)
  closure.delegate=this
  closure()
}

def main() {
	def jobsDir = build.workspace + "/jobs"
	new File(jobsDir.eachFile() { file->
	   run_workspace_script(file.getName())
        }
}

main()
