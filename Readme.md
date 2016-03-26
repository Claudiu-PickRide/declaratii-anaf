#Descriere

Acest proiect este o variantă web a softului J de depus declarații de pe [site-ul ANAF](https://www.anaf.ro/anaf/internet/ANAF/servicii_online/declaratii_electronice/descarcare_declaratii). 
Codul și librariile sunt descarcate de pe site-ul ANAF. Proiectul utilizează **gradle** ca mecanism de build. 
Proiectul este menținut de [Incremental Community](http://incrementalcommunity.ro).

#Ce face acest soft ?

Validează informațiile necesare pentru declarații și dacă sunt valide creează pdf-ul corespondent cu datele deja completate. Nu poate semna fișierele (mai multe detalii [aici](http://maisimplu.gov.ro/2016/03/21/obtinereafolosirea-unei-semnaturi-digitale/))

#Sursele

* DUKIntegrator - [Site-ul ANAF](https://www.anaf.ro/anaf/internet/ANAF/servicii_online/declaratii_electronice/descarcare_declaratii)
* Librăriile de validare/creeare pdf pentru fiecare declaratie sunt descarcate tot de pe site-ul ANAF. Pentru sursele corespondente adresați-vă direct lor.
* Wrapper pentru validatoare creat de comunitate

#Build

Proiectul foloseste gradle ca mecanism de build. 

##Requirements

 * Java 8
 * git

##Update al librariilor de pe site-ul ANAF

    ./gradlew -q updateFromANAF
    git add ./lib configversiuniCurente.txt
    git commit -m "Update librarii validare"

#Licența

Deoarece iText-5.0.4 este folosit la DUKIntegrator și la librăriile de validare, licența codului trebuie să fie AGPL. O copie a licenței în engleză se află în fișierul LICENSE.

#Cum pot să ajut ?

[Registrul problemelor este aici](https://github.com/IncrementalDevelopment/declaratii-anaf/issues)

* Poți să faci o propunere de îmbunătățire 
* Poți să rezolvi o problemă și să faci un pull request