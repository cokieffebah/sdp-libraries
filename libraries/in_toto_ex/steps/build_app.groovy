package libraries.in_toto_ex

void call(){
   sh( "mkdir demo-project")
   sh( "echo 'fake vcs-log' > demo-project/vsc.log")
   // the wrapper would need 'git'
   // sh( script: "git clone https://github.com/in-toto/demo-project.git")
   // sh( script: "git log --pretty=oneline > demo-project/vcs.log")
}