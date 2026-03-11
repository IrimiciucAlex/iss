# EmployeeManager System - Project Specification

**Project Code:** EMIA (EmployeeManager Irimiciuc Alex)

## Overview

EmployeeManager este o aplicatie desktop dezvoltata pentru administrarea angajatilor unei companii.  
Aplicatia permite managerilor sa gestioneze angajatii, sa monitorizeze activitatea acestora si sa calculeze salariile lunare.

Sistemul este conceput pentru utilizare interna si ofera functionalitati pentru autentificarea administratorilor, gestionarea angajatilor, calcularea salariilor si evidenta absentelor.

---

## Scope

Acest proiect este o aplicatie universitara care implementeaza functionalitati esentiale pentru administrarea angajatilor unei firme.

Aplicatia se concentreaza pe:

- autentificarea managerilor
- gestionarea angajatilor
- monitorizarea prezentei
- calcularea salariilor
- gestionarea bonusurilor si penalizarilor

Nu include functionalitati FOARTE complexe.

---

## Features

###F0. F0.Import Employees/Managers

Import initial data via csv/json files

### F1. Autentificarea administratorilor

Administratorii trebuie sa se autentifice pentru a accesa aplicatia.

- login folosind username si parola
- parola este salvata folosind hash criptat
- posibilitatea de logout
- acces la functionalitatile aplicatiei doar dupa autentificare

---

### F2. Vizualizarea angajatilor

Administratorul poate vedea lista angajatilor firmei (dupa login).

- posibil sa existe filtrari (ex. dupa cine castiga mai mult, nume etc.)
- buton de filtrare + selectie filtru

---

### F3. Modificare date angajat

Administratorii pot modifica informatiile unui angajat.(se face update si la salariu automat)

Operatii disponibile:

- modificarea salariului pe zi
- modificarea functiei

---

### F4. Calcul salariu lunar

Aplicatia ofera functionalitatea de calcul automat al salariilor pentru luna curenta.

Buton disponibil in aplicatie:

**"Calculeaza salarii pentru luna curenta"**

Reguli de calcul:

- salariu lunar = salariu_pe_zi × zile_lucrate  
  (initial salariu_pe_zi × cate zile are luna)
- absentele reduc numarul de zile lucrate
- bonusurile cresc salariul final
- penalizarile scad salariul final

---

### F5. Detalii angajat

Cand un angajat este selectat din lista, aplicatia afiseaza detalii suplimentare despre acesta.

Informatii afisate:

- salariu pe zi
- salariu maxim posibil in luna curenta
- numar zile lucrate
- numar zile absente
- calendar de prezenta lunar

Aceasta sectiune permite managerului sa analizeze activitatea angajatului.

---

### F6. Bonusuri salariale

Managerul poate adauga bonusuri.

Functionalitate:

- introduce suma bonus
- apare alert/notificare (ex. "Esti sigur ca vrei sa acorzi bonus?")
- suma se adauga la salariul lunar

---

## Optional

### F7. Penalizari salariale

Managerul poate scadea bani din salariu.

Functionalitate:

- introducere penalizare
- motiv optional
- suma se scade din salariu

---

### F8. Calendar prezenta angajat

Pentru fiecare angajat exista un calendar lunar.

Managerul poate:

- marca zile absente
- zilele absente reduc automat salariul

---

## Non-Functional Requirements

### Usability

Aplicatia trebuie sa aiba o interfata simpla si intuitiva pentru manageri.

### Data Integrity

Toate datele introduse trebuie validate pentru a preveni erori.

### Security

Parolele administratorilor trebuie stocate folosind functii de hash.

### Salary Accuracy

Calculul salariilor trebuie sa respecte regulile definite pentru absente, bonusuri si penalizari.

### Testing

Trebuie implementate unit tests pentru validarea logicii de business.
