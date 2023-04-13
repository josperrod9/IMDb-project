# IMDb-project
This project has the objective to build a Search API for IMDb dataset (https://datasets.imdbws.com/)

## Empathy Academy: Learning project
We´ve created an IMDb-like search engine based on different filters using the IMDb's data sets.

### Tech stack
- Java
- Maven
- SpringBoot
- Elasticsearch
- Docker

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/josperrod9/IMDb-project.git

   ```

2. Run the project
   ```sh
   docker-compose up –-build -d

   ```
    ```sh
   docker-compose up -d

   ```
3. To shut down containers:
   ```sh
   docker-compose down

   ```
#### Images in [DockerHub](https://hub.docker.com/repositories/mariaffnandez)
- searchAPI:
   ```sh
   josperrod17/imdb-api:search-api
    ```
- elastic:
   ```sh
   josperrod17/imdb-api:elastic
   ```




### Endpoints:

#### `GET /search`
It´s used to search movies using filters

##### Parameters:
- **title** (String) - Value of title to filter by
- **genres** (String) - Value of genres to filter by multiple genres. It should be sent separeted by commas (e.g genres=Action,Sci-Fi)
- **type** (String) - Value of title type to filter by values. It should be sent in the same way as genres parameter
- **maxYear** (Integer) - Max value of start year to filter by
- **minYear** (Integer) - Min value of start year to filter by
- **maxMinutes** (Integer) - Max value of runtime minutes to filter by
- **minMinutes** (Integer) - Min value of runtime minutes to filter by
- **maxScore** (Double) - Max value of average rating to filter by
- **minScore** (Double) - Min value of average rating to filter by
- **maxNHits**  (Integer)  - Upper bound of the number of hits returned (500 by default)
- **sortOrder** (String) - Sort order of the results. It can be asc or desc
- **sortBy** (String) - Sort by field. Can be 'primaryTitle', 'startYear', 'runtimeMinutes' or 'averageRating'


Example: `http://localhost:8080/search?maxYear=2022&minYear=2019&genres=Action&sortOrder=asc&sortBy=primaryTitle`
