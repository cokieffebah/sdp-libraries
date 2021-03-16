package libraries.in_toto_ex.steps

void call(){
   sh( "tar --exclude '.git' -zcvf demo-project.tar.gz demo-project scan.log")
}