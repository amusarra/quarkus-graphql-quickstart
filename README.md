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
* Integrazione della paginazione con GraphQL.
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
```

```shell
# Tramite il comando Quarkus CLI
# Il comando è disponibile solo se hai installato il Quarkus CLI.
quarkus dev
```

> **Nota**: Prima di avviare l'applicazione in modalità nativa, assicurati di aver installato e configurato correttamente
> Docker o Podman sul tuo sistema. Nel caso di errata configurazione o mancanza di Docker o Podman, l'applicazione
> non verrà avviata correttamente e potresti riscontrare errori come quelli riportati di seguito:
> 
> ```shell
> 2025-01-29 09:33:31,911 WARN  [org.tes.doc.DockerClientProviderStrategy] (build-26) DOCKER_HOST unix:///var/run/docker.sock is not listening: java.io.IOException: com.sun.jna.LastErrorException: [61] Connection refused
> 2025-01-29 09:33:31,929 ERROR [org.tes.doc.DockerClientProviderStrategy] (build-26) Could not find a valid Docker environment. Please check configuration. Attempted configurations were:
>	DockerDesktopClientProviderStrategy: failed with exception NullPointerException (Cannot invoke "java.nio.file.Path.toString()" because the return value of "org.testcontainers.dockerclient.DockerDesktopClientProviderStrategy.getSocketPath()" is null)As no valid configuration was found, execution cannot continue.
> See https://java.testcontainers.org/on_failure.html for more details.
> 2025-01-29 09:33:31,969 WARN  [io.qua.dep.uti.ContainerRuntimeUtil] (build-26) Command "docker info" exited with error code 1. Rootless container runtime detection might not be reliable or the container service is not running at all.
> 2025-01-29 09:33:31,971 INFO  [org.tes.DockerClientFactory] (build-26) Testcontainers version: 1.20.4 
> ```

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

È possibile eseguire il test delle query e mutation GraphQL anche tramite cURL, Postman o qualsiasi altro client HTTP.
A seguire un esempio di query GraphQL per recuperare tutti i libri:

```shell
curl -k -X POST http://localhost:8080/api/graphql \
  -H "Content-Type: application/json" \
  -d '{
    "query": "query { allBooks {
          title
          isbn
          publication
          genre
          pages
          summary
          languages
          formats
          authors {
            firstName
            lastName
          }
          editor {
            name
          }
        }
      }"
  }'
```

Qui è invece mostrato un esempio di mutation GraphQL per creare un libro:

```shell
curl -X POST http://localhost:8080/api/graphql \
     -H "Content-Type: application/json" \
     -d '{
       "query": "
         mutation createBook {
           createBook(
             book: {
               title: \"Libro da author e editor esistenti\",
               subTitle: \"Creato con Quarkus + GraphQL\",
               isbn: \"7650986575646\",
               pages: 567,
               summary: \"Summary of the book\",
               publication: \"2025-01-28\",
               genre: \"fantasy\",
               languages: [\"IT\"],
               formats: [\"EPUB\", \"PDF\"],
               keywords: [\"key1\"],
               authors: [{ id: 5 }],
               editor: { id: 5 }
             }
           ) {
             id
             title
           }
         }
       "
     }'
```

L'output della mutation sarà simile a quello riportato di seguito:

```json
{
   "data": {
      "createBook": {
         "id": 54,
         "title": "Libro da author e editor esistenti"
      }
   }
}
```

Volendo eseguire una query per recuperare un libro specifico, puoi utilizzare il seguente comando cURL:

```shell
curl -X POST "http://localhost:8080/api/graphql" \
     -H "Content-Type: application/json" \
     -d '{
       "query": "query getBook {
                   book(bookId: 54) {
                     id
                     title
                     subTitle
                     authors {
                       firstName
                       lastName
                     }
                     editor {
                       name
                     }
                   }
                 }"
     }'
```

L'output della query sarà simile a quello riportato di seguito:

```json
{
   "data": {
      "book": {
         "id": 54,
         "title": "Libro da author e editor esistenti",
         "subTitle": "Creato con Quarkus + GraphQL",
         "authors": [
            {
               "firstName": "David",
               "lastName": "Jones"
            }
         ],
         "editor": {
            "name": "Future Books Inc."
         }
      }
   }
}
```

Tramite la Swagger UI, puoi eseguire richieste REST per recuperare, creare, aggiornare ed eliminare libri e autori oltre 
che caricare e scaricare file su MinIO. Dalla Swagger UI puoi anche vedere lo schema OpenAPI in formato JSON e YAML e 
le eventuali chiamate che puoi eseguire tramite cURL.

![OpenAPI SwaggerUI per eseguire richieste REST](src/main/docs/resources/images/openapi-ui.jpg)

## Configurazione dell'applicazione
I punti salienti della configurazione dell'applicazione riguardano:

1. abilitazione e configurazione protocollo https
2. configurazione end-point GraphQL
3. configurazione database (H2 profilo dev e PostgreSQL profilo prod)
4. configurazione ORM
5. configurazione MinIO
6. configurazione estensione OpenShift

Per applicare modifiche all'attuale configurazione dell'applicazione, agire sul file 
'src/main/resources/application.properties'

## Implementazione del supporto alla paginazione
Quando si lavora con grandi dataset, la paginazione è fondamentale per ottimizzare le performance e migliorare 
l’esperienza utente. Con GraphQL, possiamo implementare facilmente la paginazione, utilizzando sia il modello cursor-based che l’approccio tradizionale offset-based.

Questa tabella comparativa ti aiuterà a visualizzare rapidamente le differenze tra paginazione con cursori e 
paginazione con offset.

| **Caratteristica**             | **Cursor-Based Pagination**        | **Offset-Based Pagination**        |
|---------------------------------|------------------------------------|------------------------------------|
| **Principio**                   | Navigazione basata su cursori univoci | Navigazione basata su LIMIT e OFFSET |
| **Uso principale**              | Dataset in evoluzione o scorrimento infinito | Pagine fisse, dataset stabili |
| **Prestazioni**                 | Più efficiente su grandi dataset e operazioni di ricerca | Buona per dataset piccoli e medi |
| **Gestione delle modifiche ai dati** | Non influenzato da modifiche durante la navigazione | I dati potrebbero cambiare tra le pagine (inserimenti/rimozioni) |
| **Facilità di implementazione** | Richiede gestione dei cursori | Semplice da implementare, simile a SQL |
| **Esempio di query**            | `find().range(start, end)` con cursori Base64 | `find().range(offset, limit)` (LIMIT e OFFSET SQL) |

Tabella 2 - Confronto tra Cursor-Based e Offset-Based Pagination

Dettagliando la tabella precedentemente mostrata, possiamo evidenziare i seguenti punti:

**Cursor-Based Pagination (Basata su Cursori)**
* Principio: Ogni elemento ha un identificatore unico (cursor) che viene utilizzato per navigare tra le pagine.
* Vantaggi:
  * Ottimale per dataset in continuo cambiamento (nessuna distorsione dovuta a modifiche tra le richieste).
  * Più efficiente nelle applicazioni con scorrimento infinito.
  * Ideale per evitare problemi con le lacune nelle pagine.

**Offset-Based Pagination (Basata su Offset)**
* Principio: La paginazione è gestita tramite LIMIT e OFFSET, simile alle query SQL, dove puoi definire quale pagina e quanti elementi caricare.
* Vantaggi:
* Facile da implementare e comprendere.
   * Comodo per situazioni con una quantità fissa di dati.
   * Buona per scenari dove l’ordine dei dati non cambia frequentemente tra le richieste.

Questo progetto usa Panache e Hibernate ORM e implementare entrambe le tipologie di paginazione è semplice. Con Panache, la paginazione è gestita tramite metodi come `range(startIndex, endIndex)` e cursori per la paginazione basata su cursori.

Per questo progetto la scelta è ricaduta sulla paginazione con cursori, poiché è più efficiente per dataset di grandi dimensioni e operazioni di ricerca. Inoltre, è più adatta per dataset in continuo cambiamento o con scorrimento infinito.

Quali sono i passaggi per implementare la paginazione con cursori?

1. **Calcolare i cursori**: Ogni elemento deve avere un identificatore univoco (cursor) che viene utilizzato per navigare tra le pagine.
2. **Aggiungere i cursori alla query**: Utilizzare i cursori per recuperare i dati in base alla posizione.
3. **Ritornare i cursori e le informazioni di paginazione**: Oltre ai dati, ritornare i cursori per ciascun elemento e le informazioni di paginazione (ad es. hasNextPage, endCursor).
4. **Gestire i cursori nella query successiva**: Utilizzare i cursori ritornati per recuperare i dati nella query successiva.
5. **Implementare la logica di paginazione**: Gestire la logica di paginazione nel resolver GraphQL.

Per implementare la paginazione con cursori, abbiamo bisogno di tre elementi principali: BookConnection, BookEdge e PageInfo.
Questi elementi giocano un ruolo fondamentale nella paginazione basata su cursori in GraphQL. Questi oggetti rappresentano la struttura della risposta, che include i dati e le informazioni necessarie per navigare tra le pagine di risultati.

### BookConnection
L’oggetto BookConnection rappresenta una “connessione” di elementi che può contenere più edge (in questo caso, più libri). Contiene i dati relativi agli edge (i singoli libri con i loro cursori) e le informazioni di paginazione (ad esempio, se esistono altre pagine di dati).

I ruoli principali di BookConnection sono:
* aggregare gli edges (elementi della lista, in questo caso i libri).
* includere un oggetto PageInfo per fornire dettagli sulla paginazione (se ci sono pagine successive, cursore finale, ecc.).

A seguire un esempio di implementazione di BookConnection che è un Type GraphQL (vedi annotazione `@Type`). Il codice completo è disponibile nel progetto di esempio in [BookConnection.java](src/main/java/it/dontesta/labs/quarkus/graphql/pagination/type/BookConnection.java).

```java
package it.dontesta.labs.quarkus.graphql.pagination.type;

import org.eclipse.microprofile.graphql.Type;
import java.util.List;

/**
 * Represents a connection to a list of Book edges with pagination information.
 */
@Type
public class BookConnection {

   /**
    * A list of Book edges.
    */
   public List<BookEdge> edges;

   /**
    * Pagination information for the connection.
    */
   public PageInfo pageInfo;

   /**
    * Constructs a new BookConnection instance.
    *
    * @param edges a list of Book edges
    * @param pageInfo pagination information for the connection
    */
   public BookConnection(List<BookEdge> edges, PageInfo pageInfo) {
      this.edges = edges;
      this.pageInfo = pageInfo;
   }
}
```

### BookEdge
Ogni BookEdge rappresenta un singolo libro all’interno di una connessione di dati. L’edge è il legame tra il libro e il cursore che ti permette di navigare tra le pagine.

I ruoli principali di BookEdge sono:
* contiene il node, che rappresenta il libro vero e proprio (l’oggetto Book).
* contiene il cursor, che è un identificatore univoco codificato (solitamente in Base64) che permette di fare paginazione tra le pagine successive.

A seguie un esempio di implementazione di BookEdge che è un Type GraphQL (vedi annotazione `@Type`). Il codice completo è disponibile nel progetto di esempio in [BookEdge.java](src/main/java/it/dontesta/labs/quarkus/graphql/pagination/type/BookEdge.java).

```java
package it.dontesta.labs.quarkus.graphql.pagination.type;

import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Book;
import org.eclipse.microprofile.graphql.Type;

/**
 * Represents an edge in a connection, containing a node and a cursor.
 */
@Type
public class BookEdge {

    /**
     * The node of type Book.
     */
    public Book node;

    /**
     * The cursor for this edge.
     */
    public String cursor;

    /**
     * Constructs a new BookEdge instance.
     *
     * @param node the Book node
     * @param cursor the cursor for this edge
     */
    public BookEdge(Book node, String cursor) {
        this.node = node;
        this.cursor = cursor;
    }
}
```

### PageInfo
L’oggetto PageInfo contiene informazioni cruciali sulla paginazione, come se ci sono altre pagine di dati disponibili e quale sarà il cursore finale per la navigazione alla pagina successiva.

I ruoli principali di PageInfo sono:
* hasNextPage: Indica se ci sono ulteriori pagine da caricare. Se la lista di libri contiene un numero inferiore a quello richiesto (es. 10 libri su 20), questo campo sarà true, indicando che ci sono più pagine disponibili.
* endCursor: È il cursore dell’ultimo elemento nella lista corrente, che puoi usare come after nelle query successive per ottenere la pagina successiva.

A seguire un esempio di implementazione di PageInfo che è un Type GraphQL (vedi annotazione `@Type`). Il codice completo è disponibile nel progetto di esempio in [PageInfo.java](src/main/java/it/dontesta/labs/quarkus/graphql/pagination/type/PageInfo.java).

```java
package it.dontesta.labs.quarkus.graphql.pagination.type;

import org.eclipse.microprofile.graphql.Type;

/**
 * Represents pagination information for GraphQL queries.
 */
@Type
public class PageInfo {

    /**
     * Indicates if there is a next page available.
     */
    public boolean hasNextPage;

    /**
     * The cursor to the end of the current page.
     */
    public String endCursor;

    /**
     * Constructs a new PageInfo instance.
     *
     * @param hasNextPage indicates if there is a next page available
     * @param endCursor the cursor to the end of the current page
     */
    public PageInfo(boolean hasNextPage, String endCursor) {
        this.hasNextPage = hasNextPage;
        this.endCursor = endCursor;
    }
}
```

### Come funzionano insieme BookConnection, BookEdge e PageInfo?

1. BookConnection: È il punto centrale della risposta, che contiene sia i BookEdge (gli elementi reali) sia le informazioni di paginazione attraverso l’oggetto PageInfo.
2. BookEdge: Ogni edge contiene il libro stesso (l’oggetto Book) e un cursore univoco che consente di navigare attraverso le pagine.
3. PageInfo: Fornisce informazioni critiche per il cliente, come se esistono pagine successive e quale cursore usare per la pagina successiva.

### Modifica al resolver BookGraphQLResource
Per implementare la paginazione con cursori, dobbiamo apportare alcune modifiche al resolver `BookGraphQLResource`. In particolare, dobbiamo aggiungere un nuovo metodo `books` che accetti i parametri di paginazione come `first` (per il numero di libri da recuperare) e `after` (per il cursore dell’elemento precedente). Fatto ciò, utilizzare Panache per recuperare i libri in modo paginato e restituire una lista di `BookEdge` con i relativi cursori.

A seguire l'implementazione del metodo `books` che accetta i parametri di paginazione e restituisce una `BookConnection` con i libri e le informazioni di paginazione. Il codice completo è disponibile nel progetto di esempio in [BookGraphQLResource.java](src/main/java/it/dontesta/labs/quarkus/graphql/resource/BookGraphQLResource.java).

```java
    /**
     * Retrieves a paginated list of books.
     *
     * @param first the number of books to retrieve
     * @param after the cursor after which to start retrieving books
     * @return a BookConnection containing the list of books and pagination information
     * @throws GraphQLException if an error occurs during retrieval
     */
    @Query
    public BookConnection books(@Name("first") int first,
            @NotEmpty @NotNull @Name("after") String after)
            throws GraphQLException {
        int startIndex = 0;

        // Decode the cursor to get the start index
        if (after != null) {
            try {
                String decoded = new String(Base64.getDecoder().decode(after));
                startIndex = Integer.parseInt(decoded) + 1;
            } catch (IllegalArgumentException e) {
                throw new GraphQLException("Invalid cursor format", e);
            }
        }

        // Query Panache to get the books
        PanacheQuery<Book> query = Book.findAll();
        List<Book> books = query.range(startIndex, startIndex + first - 1).list();

        // Create the edges response with the cursor
        List<BookEdge> edges = books.stream()
                .map(book -> {
                    String cursor = Base64.getEncoder().encodeToString(String.valueOf(book.id).getBytes());
                    return new BookEdge(book, cursor);
                })
                .toList();

        // Check if there are more pages
        String endCursor = edges.isEmpty() ? null : edges.get(edges.size() - 1).cursor;
        boolean hasNextPage = (startIndex + first) < query.count();

        return new BookConnection(edges, new PageInfo(hasNextPage, endCursor));
    }
```

Usando il metodo `range(startIndex, startIndex + first - 1)` di Panache, possiamo recuperare i libri in modo paginato. Aggiungiamo i cursori (codificati in Base64) per ciascun libro e restituiamo una lista di `BookEdge` insieme alle informazioni di paginazione. Come ultimo passaggio, verifichiamo se ci sono altre pagine disponibili e restituiamo una `BookConnection` con i libri e le informazioni di paginazione.

I cursori sono codificati in Base64 per garantire che siano univoci e sicuri da usare nelle query. La funzione encodeCursor codifica l’ID del libro, mentre decodeCursor lo decodifica per usarlo nella query successiva.

Queste modifiche al BookGraphQLResource ti permettono di implementare la paginazione basata su cursori in GraphQL. La struttura della risposta include un oggetto `BookConnection`, che contiene gli `BookEdge` (con i libri e i cursori) e le informazioni di paginazione tramite PageInfo.


### Test della paginazione con cursori
Per testare la paginazione con cursori, possiamo utilizzare la query `books` per recuperare per esempio i primi tre libri. La query `books` accetta due parametri: `first` (il numero di elementi da recuperare) e `after` (il cursore per la navigazione dove MA== vuol dire 0).

```graphql
query {
  books(first: 3, after: "MA==") {
    edges {
      node {
        id
        title
        authors {
          firstName
          lastName
        }
        isbn
      }
      cursor
    }
    pageInfo {
      hasNextPage
      endCursor
    }
  }
}
```

L'output atteso sarà simile a quello riportato di seguito; dove `edges` contiene i libri recuperati con i loro cursori (codificati in Base64), `pageInfo` indica che ci sono altre pagine disponibili (`hasNextPage`: true) e fornisce l’`endCursor` per ottenere la pagina successiva.

```json
{
   "data": {
      "books": {
         "edges": [
            {
               "node": {
                  "id": 4,
                  "title": "Enhanced multimedia interface",
                  "authors": [
                     {
                        "firstName": "David",
                        "lastName": "Jones"
                     },
                     {
                        "firstName": "Sarah",
                        "lastName": "Miller"
                     }
                  ],
                  "isbn": "9780230278458"
               },
               "cursor": "NA=="
            },
            {
               "node": {
                  "id": 5,
                  "title": "Optimized data throughput",
                  "authors": [
                     {
                        "firstName": "Michael",
                        "lastName": "Davis"
                     },
                     {
                        "firstName": "Jessica",
                        "lastName": "Wilson"
                     }
                  ],
                  "isbn": "9781473689271"
               },
               "cursor": "NQ=="
            },
            {
               "node": {
                  "id": 6,
                  "title": "Decentralized systems integration",
                  "authors": [
                     {
                        "firstName": "Christopher",
                        "lastName": "Garcia"
                     },
                     {
                        "firstName": "Ashley",
                        "lastName": "Rodriguez"
                     }
                  ],
                  "isbn": "9781607436911"
               },
               "cursor": "Ng=="
            }
         ],
         "pageInfo": {
            "hasNextPage": true,
            "endCursor": "Ng=="
         }
      }
   }
}
```

L'output a seguire mostra invece la query per recuperare la pagina successiva di libri utilizzando il cursore `endCursor` restituito da un precedente query. In questo risultato è evidente che `hasNextPage` è `false`, indicando che non ci sono altre pagine disponibili.

```json
{
  "data": {
    "books": {
      "edges": [
        {
          "node": {
            "id": 16,
            "title": "Machine Learning Algorithms",
            "authors": [],
            "isbn": "9780596805190"
          },
          "cursor": "MTY="
        }
      ],
      "pageInfo": {
        "hasNextPage": false,
        "endCursor": "MTY="
      }
    }
  }
}
```

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