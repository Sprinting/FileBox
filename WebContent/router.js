/**
 * Implement hash-based  client side routing
 */
window.onhashchange=(evt)=>{
    console.log("hashchange event ",evt)
   switch(location.hash)
       {
           case "":
               console.log("no hash yet");
               location.hash="dashboard";
               break;
           case "#upload":
               console.log("#upload visited");
               document
                   .getElementById("dashboard")
                   .style.display="none";
               document
                   .getElementsByClassName("upload")[0]
                   .style.display="block";
               break;
           case "#dashboard":
               console.log("#dashboard visited");
               document
                   .getElementsByClassName("upload")[0]
                   .style.display="none";
               document.getElementById("dashboard")
               .style.display="block";
               break;
       }
};