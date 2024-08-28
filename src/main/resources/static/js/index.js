const toggleSidebar = () => {
	if($(".sidebar").is(":visible")) {
			$(".sidebar").css("display" , "none")
			$(".content").css("margin-left" , "0% ")
	}
	else {
			$(".sidebar").css("display" , "block")
			$(".content").css("margin-left" , "20% ")
	}
};

const search=() => {
	let query=$("#search-bar").val()
	
	
	if(query=="") {
		$(".search-result").hide();
	}else {
		console.log(query);
		//send req to server
		
		let url='http://localhost:8081/search/'+query;
		
		fetch(url).then((response) => {
			return response.json();
		}).then((data) => {
			let text="<div class='list-group'>"
				data.forEach((contact) =>{
					text+="<a href='/users/contacts-details/"+contact.cId+"' class='list-group-item list-group-item-action'>"+contact.name+"</a>"
				});
			
			text+="</div>"

			$(".search-result").html(text);
			$(".search-result").show();
		});



	}
}

const generateOtp=() => {
	let btnName=	$("#otp-btn").text();
	
	if(btnName=="Generate OTP") {
		$("#otp-btn").text="Validate OTP";
	}
	else {
		
	}
	
}

