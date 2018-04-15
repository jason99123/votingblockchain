Readme

To setup the server, the following are required:

Docker
docker compose
ssh
go

the application from the java would need to have direct connection to the machines.

The frameworks from hyperledger fabric should also be needed to retrieve from the official website.

The docker sample script is included in the project for reference, the ip address should be modified for the connection.

For easy setup, the docker files should be insert to the fabric git folder as the following path:

fabric/examples/e2e_cli

To enable block connection, the user would need to access the docker bash of the machine to execute the following command:

peer channel join -b {blockname}


