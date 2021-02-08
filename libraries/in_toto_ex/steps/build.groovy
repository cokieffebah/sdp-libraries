package libraries.in_toto_ex

void call(){
   println "printing layout for build:"
   println get_intoto_layout('build')
   //println "\nprinting layout with no argument:"
   //println get_intoto_layout()
   sh( script: "git clone https://github.com/in-toto/demo-project.git")
   sh( script: "git log --pretty=oneline > demo-project/vcs.log")
}