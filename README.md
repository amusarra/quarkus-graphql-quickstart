# Quarkus GraphQL Quickstart + MinIO come Object Store S3

[![Keep a Changelog v1.1.0 badge](https://img.shields.io/badge/changelog-Keep%20a%20Changelog%20v1.1.0-%23E05735)](CHANGELOG.md)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![code of conduct](https://img.shields.io/badge/Conduct-Contributor%20Covenant%202.1-purple.svg)](CODE_OF_CONDUCT.md)
![Build with Maven](https://github.com/amusarra/quarkus-graphql-quickstart/actions/workflows/build_via_maven.yml/badge.svg)
![CI Docker build](https://github.com/amusarra/quarkus-graphql-quickstart/actions/workflows/docker_publish.yml/badge.svg)
![CI Docker build native amd64](https://github.com/amusarra/quarkus-graphql-quickstart/actions/workflows/docker_publish_native_amd64.yml/badge.svg)

Questo progetto è una dimostrazione di un’applicazione Quarkus che espone dati attraverso una API RESTful (`quarkus-rest`) 
tradizionale e una API GraphQL (`quarkus-smallrye-graphql`). Il progetto utilizza Hibernate ORM con Panache 
(`quarkus-hibernate-orm-panache`) per la persistenza dei dati e include configurazioni per database H2 (per sviluppo), 
PostgreSQL (per profili di produzione) e MinIO come Object Store S3 (`io.quarkiverse.minio:quarkus-minio`).

MinIO è un server di storage degli oggetti ad alte prestazioni, distribuito e compatibile con Amazon S3 facilmente
integrabile con Quarkus per via dell'estensione `quarkus-minio` e dei DevService.

GraphQL è un linguaggio di query per API che consente ai client di richiedere esattamente i dati necessari, evitando 
over-fetching o under-fetching. A differenza delle API REST, offre un singolo endpoint attraverso il quale il client 
può ottenere dati strutturati in base alle proprie esigenze.

Vantaggi principali rispetto alle API custom:
1.	Flessibilità: Il client decide quali dati ottenere, riducendo payload inutili.
2.	Performance: Minore numero di chiamate, grazie all’aggregazione di dati da più fonti in una singola query.
3.	Evolvibilità: Cambiamenti al backend possono essere introdotti senza rompere le query esistenti.
4.	Standardizzazione: L’uso di GraphQL riduce la necessità di progettare API ad hoc, semplificando sviluppo e manutenzione.

Questo approccio migliora la user experience e accelera lo sviluppo di applicazioni client e server.

| **Pro**                                                                            | **Contro**                                                                                |
|------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| **Richieste personalizzate**: i client ottengono solo i dati di cui hanno bisogno. | **Complessità iniziale**: richiede una curva di apprendimento maggiore rispetto a REST.   |
| **Single Endpoint**: un unico punto di accesso per tutte le query e mutazioni.     | **Caching più complesso**: meno intuitivo rispetto a REST (che sfrutta URL univoci).      |
| **Aggregazione dei dati**: consente di unire dati da più fonti in una chiamata.    | **Overhead server**: query complesse possono sovraccaricare il server se non ben gestite. |
| **Evolvibilità**: i campi possono essere deprecati senza interrompere il client.   | **Strumenti di debugging**: meno diffusi rispetto a quelli per REST.                      |
| **Documentazione integrata**: lo schema funge da documentazione per le API.        | **Autorizzazioni complesse**: gestione dei permessi più articolata per query dinamiche.   |
| **Riduzione delle chiamate**: ottimizza le comunicazioni client-server.            | **Setup iniziale**: richiede più configurazione e strumenti rispetto a REST.              |

> **Nota**: Questo progetto ha un approccio educativo e dimostrativo. Volutamente ci sono parti di codice non complete
>ma con commenti per guidare l'utente a completare l'implementazione. Per esempio
> ```java
>    @Mutation
>    @Description("Create a new author")
>    @Transactional
>    public Author createAuthor(Author author) {
>        // The author is persisted automatically by Panache
>        // because it is a Panache entity.
>        // Extend this method to handle the detached entity as needed.
>        author.persist();
>        return author;
>    }
>```

Il progetto segue la classica architettura a tre strati.

* Strato di Persistenza (ORM/Panache): utilizza Hibernate ORM con Panache per la gestione e la persistenza 
   dei dati nel database.
* Strato di Servizio/API (GraphQL e REST): espone i dati attraverso API GraphQL e API REST, fornendo 
   un’interfaccia per le interazioni.
* Strato di Presentazione (Opzionale): il progetto non include un livello di interfaccia utente dedicato.

Il diagramma mostrato a seguire evidenzia la stratificazione dell’applicazione.

![Stratificazione dell'applicazione](src/main/docs/resources/images/architettura-applicazione-quarkus-graphql-1.webp)

Figura 1 - Stratificazione dell'applicazione Quarkus GraphQL

## Caratteristiche

* Implementazione di un'API GraphQL con Quarkus.
* Utilizzo di Panache per la persistenza dei dati con Hibernate ORM.
* Schema GraphQL definito con tipi, query e mutation.
* Esempi di query per recuperare libri e autori.
* Esempi di mutation per creare, aggiornare ed eliminare libri e autori.
* Storage delle copertine dei libri su MinIO, sia in sviluppo (con Dev Services) che in produzione
* Test di esempio.

A seguire sono riportati i principali endpoint dell'applicazione.

| Endpoint       | Tipo    | Descrizione                                                              |
|----------------|---------|--------------------------------------------------------------------------|
| `/api/graphql` | GraphQL | Endpoint per l'API GraphQL                                               |
| `/api/books`   | REST    | Endpoint per l'API REST                                                  |
| `/api/s3/files`| REST    | Endpoint per l'API REST per il caricamento e il download di file su MinIO |
| `/q/dev-ui/io.quarkus.quarkus-smallrye-openapi/schema-yaml` | Schema  | Endpoint dello schema OpenAPI in formato YAML      |
| `/q/dev-ui/io.quarkus.quarkus-smallrye-openapi/schema-json` | Schema  | Endpoint dello schema OpenAPI in formato JSON      |
| `/q/dev-ui/io.quarkus.quarkus-smallrye-graphql/graphql-schema` | Schema  | Endpoint dello schema GraphQL                      |

Tabelle 1 - Endpoint principali dell'applicazione Quarkus GraphQL

## Requisiti

Per eseguire o sviluppare il progetto, assicurati di avere installati i seguenti strumenti.

* Git 2.33+
* JDK 21+, GraalVM 21+ (per la build nativa)
* tool per i container come Docker o Podman
* Apache Maven 3.9.9 (opzionale nel caso di uso del wrapper Maven integrato con il progetto di esempio);
* Quarkus CLI 3.17 (opzionale, ma consigliato)

## Quickstart

Per eseguire il progetto in modalità sviluppo (o dev) e testare le funzionalità GraphQL e REST, devi seguire i seguenti
passaggi:

1. clonazione del repository git del progetto;
2. avvio dell'applicazione in modalità sviluppo;
3. test delle API GraphQL e REST.
4. test delle API MinIO per il caricamento e il download di file.

Per clonare il repository git del progetto, esegui il comando:

```shell
# Clona il repository git del progetto
git clone https://github.com/amusarra/quarkus-graphql-quickstart.git
```

Per avviare l'applicazione in modalità sviluppo, esegui il comando:

```shell
# Avvia l'applicazione in modalità sviluppo
# Tramite il wrapper Maven integrato
./mvnw quarkus:dev

# Tramite il comando Quarkus CLI
# Il comando è disponibile solo se hai installato il Quarkus CLI.
quarkus dev
```

> **Nota**: Prima di avviare l'applicazione in modalità nativa, assicurati di aver installato e configurato correttamente
> Docker o Podman sul tuo sistema.

Per testare le API GraphQL e REST, apri il browser e visita i seguenti URL:

* API GraphQL usando la UI GraphiQL: <http://localhost:8080/q/dev-ui/io.quarkus.quarkus-smallrye-graphql/graphql-ui>
* API REST usando la Swagger UI: <http://localhost:8080/q/dev-ui/io.quarkus.quarkus-smallrye-openapi/swagger-ui>

Tramite la UI GraphiQL, puoi eseguire query e mutation per recuperare e modificare i dati del database.
A seguire è riportata una demo di esempio su come eseguire query GraphQL per recuperare e creare libri, autori ed editori
oltre a creare recuperare oggetti S3 dall'Object Store MinIO.

![Demo di esempio su come eseguire query GraphQL](src/main/docs/resources/images/demo-graphql-ui.gif)

A seguire le query di esempio utilizzate nella demo:

```graphql
# Get all Books
query allBooks {
   allBooks {
    id
    title
    editor {
      id
      name
    }
    authors {
      id
      firstName
      lastName
    }
  }
}

# Get Book by Id
query getBook {
  book(bookId: 5) {
    id
    title
    subTitle
    pages
    summary
    languages
    formats
    languages
    authors {
      firstName
      lastName
    }
    editor {
      name
    }
  }
}

# Get Author by Id
query getAuthor {
  author(authorId: 5) {
    firstName
    lastName
    sex
    birthDate
    books {
      id
      title
    }
  }
}

# Get Editor by Id
query getEditor {
  editor(editorId: 5) {
    name
    books {
      title
      subTitle
      summary
    }
  }
}

# Mutation to create a Book with an Author and an Editor that already exist
mutation createBook {
  createBook(
    book: {title: "Libro collaborativo", 
      isbn: "7650986575646", 
      pages: 567, 
      summary: "Summary of the book", 
      publication: "2025-01-28", 
      genre: "fantasy", 
      languages: ["IT"], 
      formats: ["EPUD", "PDF"], 
      keywords: ["key1"], 
      authors: [{id: 5}], 
      editor: {id: 5}}
  ) {
    id
    title
  }
}

# Mutation to create a Book with a new Author and an Editor that already exist
mutation createBookWithNewAuthor {
  createBook(
    book: {title: "Montare a cavallo con Quarkus + GraphQL", 
      isbn: "110986575646", 
      pages: 120, 
      summary: "Summary of the book", 
      publication: "2025-01-28", 
      genre: "equitazione", 
      languages: ["IT"], 
      formats: ["EPUD", "PDF"], 
      keywords: ["sport", "equitazione"], 
      authors: [
        {
          firstName: "Antonio",
          lastName: "Musarra",
          sex: "M",
          birthDate: "2025-01-28"
        }
      ], 
      editor: {id: 5}}
  ) {
    id
    title
  }
}

# Mutation to upload a file to MinIO
mutation uploadFile {
  uploadFile(
    objectName: "example.txt", 
    bucketName: "my-bucket" 
    content: "Q29udGVudG8gZGVsIGZpbGUgZXNlbXBpbGU=") {
    
    objectName
    url
  }
}

# Mutation to download a file from MinIO
query getFile {
  getFile(objectName: "example.txt", bucketName: "my-bucket-1") {
    url
    size
    eTag
    contentType
    content
  }
}
```

Tramite la Swagger UI, puoi eseguire richieste REST per recuperare, creare, aggiornare ed eliminare libri e autori oltre che caricare e scaricare file su MinIO.

![OpenAPI SwaggerUI per eseguire richieste REST](src/main/docs/resources/images/openapi-ui.jpg)

## Configurazione dell'applicazione
I punti salienti della configurazione dell'applicazione riguardano:

1. abilitazione e configurazione protocollo https
2. configurazione end-point GraphQL
3. configurazione database (H2 profilo dev e PostgreSQL profilo prod)
4. configurazione ORM
5. configurazione MinIO
6. configurazione estensione OpenShift

Per agire sulla configurazione agire sul file 'src/main/resources/application.properties'

## Creazione dell'artefatto ed eseguire l’applicazione

L’applicazione può essere impacchettata utilizzando il comando:

```bash
./mvnw package
```

Questo comando genera il file `quarkus-run.jar` nella directory `target/quarkus-app/`.
Tieni presente che non si tratta di un über-jar, poiché le dipendenze vengono copiate nella directory 
`target/quarkus-app/lib/`.

L’applicazione può ora essere eseguita utilizzando il comando:

```bash
java -jar target/quarkus-app/quarkus-run.jar.
```

Se desideri creare un über-jar, esegui il seguente comando:

```bash
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

L’applicazione, impacchettata come über-jar, può essere eseguita con:

```bash
java -jar target/*-runner.jar.
```

## Creare un eseguibile nativo

Puoi creare un eseguibile nativo utilizzando il comando:

```bash
./mvnw package -Dnative
```

Oppure, se non hai installato GraalVM, puoi creare l’eseguibile nativo utilizzando un container con il comando:

```bash
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

Dopo la creazione, puoi eseguire il tuo eseguibile nativo con:

```bash
./target/quarkus-graphql-quickstart-1.0.0-SNAPSHOT-runner
```

Per maggiori informazioni sulla creazione di eseguibili nativi, consulta la guida ufficiale: https://quarkus.io/guides/maven-tooling.

## Guida ai servizi e alle estensioni utilizzate

- ArC ([guide](https://quarkus.io/guides/cdi-reference)): Build time CDI dependency injection
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC
- SmallRye Health ([guide](https://quarkus.io/guides/smallrye-health)): Monitor service health
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern
- JDBC Driver - H2 ([guide](https://quarkus.io/guides/datasource)): Connect to the H2 database via JDBC
- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Hibernate Validator ([guide](https://quarkus.io/guides/validation)): Validate object properties (field, getter) and method parameters for your beans (REST, CDI, Jakarta Persistence)
- GraphQL ([guide](https://quarkus.io/guides/smallrye-graphql)): Expose your services as a GraphQL endpoint
- MinIO ([guide](https://quarkus.io/extensions/io.quarkiverse.minio/quarkus-minio/)): Use MinIO as an Object Store S3
- OpenShift ([guide](https://quarkus.io/guides/deploying-to-openshift)): Generate OpenShift resources from annotations
- Using Podman with Quarkus ([guide](https://quarkus.io/guides/podman))
## Team Tools

[![alt tag](http://pylonsproject.org/img/logo-jetbrains.png)](https://www.jetbrains.com/?from=LiferayPortalSecurityAudit)

Antonio Musarra's Blog Team would like inform that JetBrains is helping by
provided IDE to develop the application. Thanks to its support program for
an Open Source projects!