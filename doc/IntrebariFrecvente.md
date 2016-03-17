#1.	Informatii generale program:

I: 	Cum pot depune XML-ul validat? Trebuie sa-l import/ atasez in PDF-ul inteligent?
R:	Declaratia Unica D112 consta dintr-un PDF la care se atseaza un fisier XML. Programul genereaza automat PDF-ul declaratiei si ataseaza XML-ul validat. De asemenea ofera si posibilitatea semnarii digitale. Nu aveti nevoie de niciun alt program suplimentar.

I:	 De ce nu mi se genereaza (pentru listare/vizualizare) si Anexele 1.1 si 1.2?
R:	Pentru depunerea declaratiei D112 este necesar doar PDF-ul cu Anexa 1, impreuna cu XML-ul atasat din care vor fi preluate datele pe serverul central. Programul nu include in PDF anexele 1.1 si 1.2
Pentru sanatate: In Ordinul 130/351/2011 se specifica ce documente sunt necesare pentru recuperarea concediilor medicale. Nu se cer anexele 1.1 si 1.2 din D112. Datele din aceasta anexa se regasesc in sistemul unic integrat.
Pentru somaj: In HG 113/09.02.2011, publicata in Monitorul Oficial 133/22.02.2011, apar modificarile la Normele metodologice de aplicare a Legii 76/2002  prin care se elimina obligatia prezentarii declaratiei listate la AJOFM.

I:	In denumirea firmei exista un &. La validarea xml-ului imi apare o eroare fatala de parsare.
R:	In formatul xml exista "escape characters": 

    &amp; pentru & 
    &lt; pentru < 
    &gt; pentru > 
    &quot; pentru " 
    &apos; pentru '  

Inlocuiti & cu &amp; in XML. Daca deschideti fisierul, de exemplu intr-un browser de internet, continutul apare corect si nu veti avea nici eroare la validare 

I:	Dorim sa se ofere posibilitatea de a crea PDF-ul declaratiei si de a-l semna direct din linia de comanda, prin folosirea de parametri
R:	Incepand cu versiunea J1.5.0 se poate valida declaratia si genera/ semna PDF-ul prin intermediul liniei de comanda. Va rugam sa cititi fisierul Instructiuni.txt pentru modul de folosire. De asemenea, sursele programului de generare PDF, semnare si interfata grafica au fost publicate pe site-ul ANAF iar in cadrul lor exista clasa Integrator creata special pentru a fi utilizata in aplicatiile dumneavoastra. Le puteti folosi sub licenta AGPL (folosim iText care este sub AGPL) pentru a va crea functionalitatile pe care le doriti. 

I:	Lansez aplicatia din linia de comanda si primesc urmatoarea eroare: „linie comanda incompleta”
R:	Incapand cu versiunea J1.5.0 va trebui folosit noul format al liniei de comanda (documentat in fisierul Instructiuni.txt). Eroarea indica faptul ca lipseste un parametru obligatoriu din linia de comanda (cel mai probabil tipul declaratiei).

I:	Pot valida/prelucra mai multe fisiere XML o data ?
R:	Da! Selectati folderul care contine fisierele XML. Apasand butoanele corespunzatoare se vor prelucra toate fisierele XML din folder.

I:	Cat de rapid este programul?
R:	Pe un calculator DualCore 2.4 Ghz, 4Gb RAM,  JVM cu setari default, un fisier cu date reale de 2000 asigurati a durat la validare 0.6 secunde. Crearea si semnarea dureaza aproximativ inca o secunda fiecare.

I: 	Cand pornesc programul imi apare o fereastra in care se verfica versiunea programului. 
R:	De la versiunea J1.2.2 s-a introdus facilitatea de verficare automata (printr-o conectare prin http) daca au mai aparut versiuni noi ale programului pe site-ul ANAF si posibilitatea de a le descarca. Pentru a renunta la aceasta facilitate stergeti din fisierul de configurare linia: urlVersiuni=http://static.anaf.ro/static/10/Anaf/declunica/versiuni.xml

I:	La pornirea din linia de comanda, daca numele folderului care contine dist/DUKIntegrator.jar are spatii apare eroarea:

    Exception in thread "main" java.io.FileNotFoundException: c:\expert (The system cannot find the file specified)
            at java.util.zip.ZipFile.open(Native Method)
            at java.util.zip.ZipFile.<init>(Unknown Source)
            at java.util.jar.JarFile.<init>(Unknown Source)
            at java.util.jar.JarFile.<init>(Unknown Source)

R:	Incercati sa lansati explicit versiunea de java dorita din linia de comanda, iar caile sa fie puse intre ghilimele:
`C:\Documents and Settings\local>"C:\Program Files\Java\jdk1.6.0_21\jre\bin\java.exe" -jar "c:\expert w\dist\DUKIntegrator.jar"`

#2.	Erori la validare

I:	Am eroarea - eroare structura: tag necunoscut (“frmMain”)
R:	Ati folosit pentru validare, cel mai probabil, fisierul XML rezultat in urma exportului (Document – Forms – Export) din Pdf-ul intelligent. XML-ul declaratiei D112 este cel care se gaseste in atasamentul PDF-ului inteligent.

I: 	In fisierul de erori imi apar atentionari desi am completat corect datele
R: 	O declaratie care are numai atentionari este considerata valida. 
Sunt atentionari si nu erori si pentru faptul ca respectivele reguli se aplica in marea majoritate a cazurilor dar exista si exceptii pentru care respectiva regula nu mai trebuie respectata. Atentionarile doar atrag atentia ca este posibil sa nu se fi completat corect.


I:	Calea/fisierul XML contin spatii; mesajul de la parsare este:
E: validari globale
 Eroare fatala de parsare: 'unknown protocol: c'
R:	versiunea jre 1.6 instalata contine un bug. Solutie: instalare ultima versiune jre 1.6 sau eliminati din denumirea fiserelor spatiile.

I: 	Obtin urmatorul mesaj de eroare:
F: validari globale 
Eroare fatala de parsare: "invalid byte 2 of 2 byte UTF-8 sequence" 
R: 	Aceasta eroare apare din cauza unui caracter (caractere) encodate gresit. De exemplu XML-ul este encodat UTF-8 si exista caractere UTF-16. In acest caz respectivele caractere nu pot fi „citite”.
Aceasta situatie este intalnita de obicei la diacritice, dar mai poate apare si la caractere speciale - de tipul: #,$, etc.
Solutia:
1. Encodarea corecta a acestor caractere (editarea textului intr-un editor de text - Notepad, Wordpad,  etc. ar trebui sa rezolve problema).
2. Renuntarea la diacritricele respective (de exemplu in loc de Ţ sa fie T)

I: 	Obtin urmatorul mesaj de eroare:
„ atributul mailFisc prezent dar vid nepermis”
R:	Ati introdus respectivul atribut in XML sub forma mailFisc=”   ” sau mailFisc=”” . Solutie: introduceti o valoare corecta sau elimitati complet acest atribut din XML.

I: 	Daca nu am erori la validarea cu soft-ul J inseamna ca nu voi avea erori nici la validarea pe server?
R: 	Pentru validarea pe server se foloseste ultima versiune de Validator care se distribuie si prin programul de asistenta (soft J). Pe server insa se mai fac o serie de validari care nu pot fi facute la nivel „local”: verificari cu nomenclatoare / registri ( existenta CUI, pereche corecta sediu principal – sediu secundar, etc.) precum si: verficare sa nu mai existe o declaratie initiala valida pentru aceasi perioada/ CUI, verificare ca pentru o rectificativa sa existe o initiala valida, perioada de raportare sa nu fie „in viitor” sau anterioara lui 01/2011, etc.

#3.	Erori la semnare:

I: 	Nu pot semna cu certificatul digital. Imi apare o eroare (exceptie) la semnare.
R:	Incercati urmatoarea solutie:
1.	Editati fisierul smartCard.cfg din folderul config (de exemplu, daca aveti aladdin veti edita aladdin.cfg) si stergeti '#'-ul din fata liniei 'algorithm=mscapi'.
2.	Salvati modificarea.
3.	Reincercati semnarea.

I: 	Nu pot semna cu certificatul digital. Nu-mi apar tipurile de certificate digitale.
R:	Verificati existenta, in acelasi director cu DUKIntegrator.jar sau conform comenzii cu “-c” a folderului config, care trebuie sa contina fisierele de configurare .cfg.

I: 	Nu pot semna cu certificatul digital. Imi apare o eroare (exceptie) la semnare.
R:	Verificati ca in fisierul .cfg corespunzator dispozitivului cu care semnati este inscrisa calea corecta catre fisierul .dll, reprezentand driverul necesar accesarii dispozitivului de pe calculatorul local. Aceasta cale poate fi editata pentru a arata corect locatia existenta.

I:	Nu pot semna cu certificat digital pe Windows (XP, Vista, 7) pe 64 biti.
R:	In acest moment, pe Windows 64 biti Java nu asigura suport deplin pentru biblioteca SunPKCS11.jar (care este providerul de securitate care trebuie folosit pentru semnarea cu certificatele digitale calificate). Pe celelalte sisteme de operare pe 64 biti nu sunt probleme in Java cu aceasta biblioteca. 
Solutia: 
4.	Folositi versiunea JVM pt. 32 de biti.
5.	Editati fisierul smartCard.cfg din folderul config (de exemplu, daca aveti aladdin veti edita aladdin.cfg) si stergeti '#'-ul din fata liniei 'algorithm=mscapi'.
6.	Salvati modificarea.
7.	Reincercati semnarea.

I:	La semnare cu SmartCardul obtineti eroarea: "eroare acces driver: <cale\driver.dll>".
R:	Corectati parametrul library din fisierul dist\config\SMART_CARD.cfg astfel incat sa indice calea reala pe calculatorul dumneavoastra catre driverul corespunzator SMART_CARD-ului folosit

I:	La semnare se obtine eroarea:
`"java.security.ProviderException: Error parsing configuration".`
R:	Calea catre driverul SmartCardului (care se afla inscrisa in fisierul .cfg corespunzator acestuia) contine unul din urmatoarele caractere: "~()". Solutie: Copiati continutul intregului folder in alta locatie si modificati corespunzator calea din fisierul .cfg. (vezi si http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6581254)

I:	La semnare cu SmartCardul obtineti eroarea: 
`"java.security.KeyStoreException: KeyStore instantiation failed".`
R:	Probabil nu aveti nici un SmartCard conectat sau PIN-ul nu este corect


I:	La semnare cu SmartCardul Schlumberger, aveti un PIN mai lung de 8 caractere si obtineti eroarea: 
`"java.security.KeyStoreException: KeyStore instantiation failed".`
R:	Incercati sa introduceti primele 8 caractere ale PIN-ului

I:	La semnare obtineti eroarea: "java.security.ProviderException: Initialization failed".
R:	Verificati daca aveti conectate la statie si alte SmartCarduri, in plus fata de cel cu care intentionati sa semnati D112 (de exemplu aveti in plus un SmartCard de la o banca). Pt. moment solutia recomandata este sa deconectati toate celelalte SmartCarduri cu exceptia celui folosit pt. D112 si sa reluati procesul de semnare. Incercati sa folositi optiunea "*autoDetect"

I: 	Detin un certificat reinoit. La semnare imi apare un mesaj ca certificatul este expirat.
R:	Daca pentru reinoirea certificatului se foloseste acelasi alias, java va accesa aleatoriu certificatele care sunt sun acelasi alias. In acest caz incercati sa mai semnati o data (apasati din nou butonul de semnare). De la versiunea J1.4.0 aplicatia face mai multe incercari de semnare – probabilitatea de a avea aceasta problema este foarte mica.

Va multumim pentru observatii transmise care ne-au ajutat sa corectam bug-uri/ sa imbunatatim aplicatia. Puteti adresa in continuare intrebari/ observatii/ sugestii/ erori intalnite pe adresa de mail: d112@mfinante.ro, cu rugamintea de a inscrie in subiectul mesajului „soft J”. 
