var socket = new WebSocket("ws://thebad.xyz/tables/test?token=eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoiMzI0ZGQ5NjEtOGM5My00OGRlLWJjYTctNzNkYzUwZGRhZDQyIiwiYXVkIjoidGVzdCJ9.UtflAXN_Pk8TIe98C5C-DOOs2OYT-4aE2-E4o_IkMRE");

console.log("opening");

socket.onopen = function (event) {
	console.log("Connection openned !");
	
	for(int i = 0; i < 1000; i++) {
		
		
		
	}
	
};

socket.onmessage = function (event) {
  console.log(event.data);
}
