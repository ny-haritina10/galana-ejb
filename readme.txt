-> dossier EJB : a copier dans gestion-station/station-ejb/src/java
-> copier le fichier send.xml dans gestion-station (meme niveau station-ejb, station-war)
-> modifier tous les fichiers apj.properties
	-> modifier les credentials (user, mdp, url) vers l'user nanaovana import an'ilay base.dmp
-> modifier les classes Database.java vers l'user de votre base de données (base de données misy an'ilay prelevement)
-> modifier tous les fichiers .bat (archive.bat, _deploy.bat, start.bat)
-> modifier le fichier send.xml
-> ordre de compilation:
	A** Dans gestion-station 
		-> 1) ant -f build.xml
		-> 2) ant -f send.xml
	B** Dans galana-ejb
		-> 1) archive.bat
	C** Dans galana-war
		-> 1) _deploy.bat
	D** start.bat