curl --verbose --user someUsername:xxx --header "Accept:application/json" --header "Content-Type:application/json" -d '{"entity":"user", "UUID":"this-is-not-a-UUID", "name":"ford", "password":"secret", "fullname":"Ford Prefect", "email":"ford.prefect@gzmail.com"}' -X POST http://localhost:9000/entity