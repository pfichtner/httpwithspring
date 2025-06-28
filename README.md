# Beispielprojekt: Spring Boot mit hexagonaler Architektur und H2-Datenbank

Dieses Projekt demonstriert eine fachlich saubere und wartbare Architektur für eine Spring-Boot-Anwendung mit folgenden Eigenschaften:

---

## ✅ Spring Boot + H2-Datenbank

- Die Anwendung basiert auf Spring Boot und verwendet eine eingebettete H2-Datenbank, die automatisch beim Starten der Anwendung initialisiert wird.
- Ideal für lokale Entwicklung und Tests, ohne externe Datenbank.

---

## ✅ Hexagonale Architektur

- Die Anwendung folgt dem Prinzip der **hexagonalen Architektur** (Ports & Adapters).
- Die Domäne steht im Zentrum, alle Zugriffe von außen (z.B. REST-API) oder nach außen (z.B. Datenbank) sind klar getrennt und über Interfaces abstrahiert.
- **100% saubere Trennung**:
  - Es gibt **eigene Klassen für das JSON-Mapping** (DTOs), die ausschließlich für die Kommunikation über REST/HTTP verwendet werden.
  - Es gibt **eigene Klassen für die Datenbankebene** (JPA-Entities), die ausschließlich für das Persistieren genutzt werden.
  - Der **Domänenkern** ist dadurch **frei von jeglichen Abhängigkeiten** zu Frameworks wie JSON/REST/HTTP/JPA/DBMS etc.
  - Die Domäne kennt nur ihre eigenen fachlichen Modelle, Services und Repositories (Ports) – keine technischen Details.
- Adapter (Controller, Repository-Implementierungen) übernehmen die Aufgabe der Übersetzung zwischen Domäne und den externen Technologien (JSON, JPA, HTTP).

---

## ✅ Niedrige Kopplung

- Der **RestController** kennt ausschließlich das **Service-Interface**.
- Die **Service-Implementierung** ist `package-private`, wodurch sichergestellt wird, dass sie nur innerhalb ihres eigenen Packages erzeugt werden kann.
- Die **Service-Implementierung nutzt nicht direkt das Spring-Data-JPA-Repository**, sondern arbeitet mit einem **eigenen Repository-Interface** (und damit indirekt mit einer eigenen Implementierung).
- Diese **Repository-Implementierung** ist ebenfalls `package-private` und delegiert intern an das Spring-Data-JPA-Repository.
- Dadurch wird die direkte Abhängigkeit des Service von Spring Data JPA vermieden und die Kopplung zwischen Domäne und Framework bleibt gering. Zudem lässt sich das **domäneneigene Repository einfacher durch einen Testdouble ersetzen** als das Spring-Data-JPA-Repository, was das Testen und die Isolation der Domänenlogik erleichtert.  

- Die **Repository-Implementierung** (Adapter) ist ebenfalls `package-private`, sodass sie nicht von außen direkt instanziiert werden kann.

Dies sorgt für eine geringe Kopplung zwischen Schichten und verhindert unkontrollierte Abhängigkeiten.

---

## ✅ Trennung Domänen- vs. Jpa-Repository

- Das **Domänen-Repository** (z.B. `BerechtigungenRepository`) enthält nur Methoden, die fachlich notwendig sind (z.B. `findById`, `deleteById`).
- Dieses Domänen-Repository delegiert intern an ein **JpaRepository**, das alle technischen CRUD-Funktionalitäten kapselt.
- Der Rest der Domäne bleibt dadurch frei von JPA-spezifischen Details.

---

## ✅ Hohe Kohäsion

- Fachlich zusammengehörende Klassen (z.B. Controller, Service, DTOs) liegen in gemeinsamen Packages.
- Dies verbessert die Übersicht und sorgt für eine bessere Wartbarkeit, da alle relevanten Klassen eines Fachbereichs gebündelt sind.

---

## ✅ Typsichere Identifier in der Domäne

- In der Domäne wird **nicht direkt mit UUIDs gearbeitet**.
- Stattdessen gibt es den Typ `BerechtigungsId`.
- Das verhindert, dass beliebige UUIDs (z.B. von anderen Aggregaten oder per Zufall generiert) unabsichtlich zur Abfrage verwendet werden können.
- Dadurch wird die Fachlichkeit sauberer abgebildet und Typfehler zur Compile-Zeit ausgeschlossen.

---

## ✅ Teststrategie

- Es gibt **lediglich einen einzigen Test**, der **vollintegrativ** ist: Er testet alles von der **Eingangsschnittstelle (HTTP)** über den Domänenkern bis hin zur Datenbank in einem Durchlauf.  
- Dieser Integrations-Test dient ausschließlich zur **Veranschaulichung** des Gesamtablaufs.
- Dank der hexagonalen Architektur lassen sich die einzelnen Bausteine in der Praxis jedoch **leicht isoliert testen**. Übliche Tests sind:

  ✅ **Tests des Domänenkerns**  
  - Prüfung der fachlichen Logik (z.B. Berechnungen, Regeln, Validierungen) komplett ohne technische Abhängigkeiten.

  ✅ **Tests der Eingangsschnittstelle (Controller)**  
  - Sicherstellen, dass HTTP-Anfragen korrekt entgegengenommen, korrekt in den Domänenkern weitergeleitet und Antworten des Domänenkerns korrekt zurückgeliefert werden.

  ✅ **Tests des Secondary Ports (z.B. DBMS)**  
  - Verifikation, dass Domänenklassen über die Repository-Implementierungen wie erwartet persistiert, aktualisiert, abgefragt und gelöscht werden können.

---

## ▶️ Anwendung starten

Um die Anwendung lokal zu starten, führen Sie folgenden Befehl im Wurzelverzeichnis des Projekts aus:

```bash
mvn spring-boot:run
```

## 📎 Beispielhafte curl-Befehle

### PUT: Berechtigung aktualisieren oder anlegen (Daten direkt angegeben)

```bash
curl -X PUT "http://localhost:8080/berechtigungen/123e4567-e89b-12d3-a456-426614174000" \
  -H "Content-Type: application/json" \
  -d '{
    "foo": "hello",
    "bar": 42,
    "foobar": "world"
  }'
```

### PUT: Berechtigung aktualisieren oder anlegen (Daten aus Datei)

```bash
curl -X PUT "http://localhost:8080/berechtigungen/123e4567-e89b-12d3-a456-426614174000" \
  -H "Content-Type: application/json" \
  --data-binary "@data.json"
```

### DELETE: Berechtigung löschen

```bash
curl -X DELETE "http://localhost:8080/berechtigungen/123e4567-e89b-12d3-a456-426614174000"
```

### GET: Berechtigung abfragen

```bash
curl "http://localhost:8080/berechtigungen/123e4567-e89b-12d3-a456-426614174000"
```

(```-v``` für ```verbose``` ergänzen)

