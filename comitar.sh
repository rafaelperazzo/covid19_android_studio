CURRENTDATE=`date +"%Y-%m-%d %T"`
/usr/bin/git add .
/usr/bin/git commit -m "Atualizado ${CURRENTDATE}"
/usr/bin/git push origin master
