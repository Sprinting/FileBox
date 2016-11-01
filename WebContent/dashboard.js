/**
 * This file loads the user's dashboard by sending a GET request to /FileBox/dashboard 
 */

var x=null;
function show_loader() {
    /*
    * show loading anims here. Call before request
    */
};
function hide_loader() {
    /*
    * hide loading anims here. Call after request complete
    */
};

function getUserData(url, timeout) {
    //send a request to /FileBox/dashboard
    return new Promise((resolve,reject)=>{
        let request= new XMLHttpRequest();
            request.open('GET',url);
            request.onload=()=>
            {
                if(request.status==200)
                    {
                        resolve(request);
                    }
                else
                    reject(Error(request.statusText));
            };
            setTimeout(request.onerror,timeout);
            request.onerror=()=>
                {reject(Error(request.statusText));};
        request.send();
    });
    
};


function populateHTML(data)
{
    let dashboard=document.getElementById("dashboard");
     data=JSON.parse(data.response);
    let filepaths=data["files"];
    let filedates=data["uploaded"];
    let filenames=
        filepaths.map((key) =>
                     {
            return key.split("/")[key.split("/").length-1];
        });
    
    let username=filepaths[0].split("/")[filepaths[0].split("/")
                                       .length-2]
    console.log(filenames);
    let downloadTable=document.createElement("table");
    let tableHeaderRow=document.createElement("tr");
    let filenameHeader=document.createElement("th");
    let uploadDateHeader=document.createElement("th");
    uploadDateHeader.appendChild(document
                                 .createTextNode("Uploaded"));
    filenameHeader.appendChild(document
                               .createTextNode("File"));
    tableHeaderRow.appendChild(filenameHeader);
    tableHeaderRow.appendChild(uploadDateHeader);
    downloadTable.appendChild(tableHeaderRow);
    dashboard.appendChild(downloadTable);
    filenames.map((file,idx)=>
    {
        let uri="/FileBox/getFile?u="+username+"&f="+file;
        let linkSpan=document.createElement("span");
        let fileLink=document.createElement("a");
        fileLink.setAttribute("href",uri);
        fileLink.setAttribute("target","_blank");
        fileLink.appendChild(document.createTextNode(file));
        linkSpan.appendChild(fileLink); 
        linkSpan.setAttribute("class","fileLink "+idx);
        let downloadTableRow=document.createElement("tr");
        let downloadFilename=document.createElement("td");
        let uploadDate=document.createElement("td");
        downloadFilename.appendChild(linkSpan);
        uploadDate.appendChild(document
                               .createTextNode(filedates[idx]));
        downloadTableRow.appendChild(downloadFilename);
        downloadTableRow.appendChild(uploadDate);
        downloadTable.appendChild(downloadTableRow);
        return true;
    }
                 );
}

window.onload= ()=>{
	//alert("Hi");
    let dashboard=
        document.getElementById("dashboard");
    show_loader();
    let uri= "/FileBox/dashboard";
    let dataPromise=
    getUserData(uri,5000);
    dataPromise.then((dapi)=>{
        //console.log("lolol",dapi);
            hide_loader();
            populateHTML(dapi);
        });
    dataPromise.catch((err)=>{
        console.log("error ",err);    
    })
    
};

