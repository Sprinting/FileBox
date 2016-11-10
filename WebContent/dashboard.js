/**
 * This file loads the user's dashboard by sending a GET request to /FileBox/dashboard 
 */

let forEach = (array,callback,scope)=>
    {
        for(let i=0;i<array.length;i++)
            {
                callback.call(scope,i,array[i]);
            }
    };

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
    //send an async GET request to /FileBox/dashboard
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

    


/*
debug var, move to function
*/
function link_context_menu(idx,node)
{
   
   
};

let linkNodeList=null;
function populateHTML(data)
{
    let file_dashboard=document.getElementById("user_files");
    let share_file_dashboard=document
    .getElementById("share_files");
    
    data=JSON.parse(data.response);
    let filepaths=data["files"];
    let filedates=data["uploaded"];
    
    let sharepaths=data["shared"];
    let sharedates=data["share_uploaded"];
    
    let filenames=
        filepaths.map((key) =>
                     {
            return key.split("/")[key.split("/").length-1];
        });
    
    let usernames=filepaths.map((key)=>{
        return key.split("/")[key.split("/").length-2];
    });
    
    let sharenames=sharepaths.map((key)=>{
        return key.split("/")[key.split("/").length-1];
    });
    let shareusers=sharepaths.map((key)=>{
        return key.split("/")[key.split("/").length-2];
    });
    
    console.log(usernames);
    console.log(filenames);
    console.log(sharenames);
    console.log(shareusers);
    let downloadTable=document.createElement("table");
    let sharedTable=document.createElement("table");
    
    let tableHeaderRow=document.createElement("tr");
    let filenameHeader=document.createElement("th");
    let uploadDateHeader=document.createElement("th");
    let shareHeader=document.createElement("th");
    uploadDateHeader.appendChild(document
                                 .createTextNode("Uploaded"));
    shareHeader.appendChild(document
                                 .createTextNode("Share"));
    filenameHeader.appendChild(document
                               .createTextNode("File"));
    tableHeaderRow.appendChild(filenameHeader);
    tableHeaderRow.appendChild(uploadDateHeader);
    
    
    sharedTable.appendChild(tableHeaderRow.cloneNode(true));
    tableHeaderRow.appendChild(shareHeader);
    downloadTable.appendChild(tableHeaderRow);
    file_dashboard.appendChild(downloadTable);
    share_file_dashboard.appendChild(sharedTable);
    
    filenames.map((file,idx)=>
    {
        let uri="/FileBox/getFile?u="+usernames[idx]+"&f="+file;
        let share_uri="/FileBox/shareFile?f="+file;
        let linkSpan=document.createElement("span");
        let fileLink=document.createElement("a");
        let shareSpan=document.createElement("span");
        let shareLink=document.createElement("a");
        
        fileLink.setAttribute("href",uri);
        fileLink.setAttribute("target","_blank");
        fileLink.appendChild(document.createTextNode(file));
        linkSpan.appendChild(fileLink); 
        linkSpan.setAttribute("class","fileLink "+idx);
        
        shareLink.appendChild(document.createTextNode("Share this file"));
        shareLink.setAttribute("target","_blank");
        shareLink.setAttribute("href",share_uri);
        shareSpan.appendChild(shareLink);
        shareSpan.setAttribute("class","shareLink "+idx);
        
        let downloadTableRow=document.createElement("tr");
        let downloadFilename=document.createElement("td");
        let uploadDate=document.createElement("td");
        let shareFile=document.createElement("td");
        
        downloadFilename.appendChild(linkSpan);
        uploadDate.appendChild(document
                               .createTextNode(filedates[idx    ]));
        shareFile.appendChild(shareSpan);
        
        downloadTableRow.appendChild(downloadFilename);
        downloadTableRow.appendChild(uploadDate);
        downloadTableRow.appendChild(shareFile);
        downloadTable.appendChild(downloadTableRow);
        return true;
    }
                 );
    
    //make a function,lol
    sharenames.map((file,idx)=>
    {
        let uri="/FileBox/getFile?u="+shareusers[idx]+"&f="+file;
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
                               .createTextNode(filedates[idx    ]));
        downloadTableRow.appendChild(downloadFilename);
        downloadTableRow.appendChild(uploadDate);
        sharedTable.appendChild(downloadTableRow);
        return true;
    }
                 );
   
    linkNodeList=document.getElementsByClassName("fileLink");
    forEach(linkNodeList,(idx,node)=>{
        contextNode=document.getElementById("context_links")
        console.log("ForEach node ",node,idx);
        let downloadNode=null;
        node.addEventListener("click",(evt)=>{
        evt.preventDefault();
        //shareNode=document.createElement("a");
        //shareNode.appendChild(document.
        //                    createTextNode("Share this file"));
        //shareNode.setAttribute("href","#");

        downloadNode=document.createElement("a");
        downloadNode.appendChild(document.
                              createTextNode("download this file"));
        downloadNode.setAttribute("href",node.firstChild.getAttribute("href"));
        contextNode.innerHTML="";
        contextNode.appendChild(downloadNode);
        contextNode.appendChild(document.createElement("br"));
        //contextNode.appendChild(shareNode);
        //console.log(idx,"shareNode",shareNode);
        console.log(idx,"downloadNode",downloadNode);
        console.log(idx,"contextNode",contextNode);
        },false)
});

    linkNodeList=document.getElementsByClassName("shareLink");
    forEach(linkNodeList,(idx,node)=>{
        contextNode=document.getElementById("context_links")
        console.log("ForEach node ",node,idx);
        
        node.addEventListener("click",(evt)=>{
            evt.preventDefault();
            let share_uri=node.firstChild.getAttribute("href");
            let sharePromise=getUserData(share_uri,5000);
            sharePromise.then((dapi)=>{
                console.log("Promise complete,aye!");
                let data=JSON.parse(dapi.response);
                let text="Please share the link below with the intended recepient.Expires in 2 days,starting now";
                if(data["status"])
                    {
                        console.log(data["status"]);
                        let sharedFileUri="/FileBox/getSharedFile?token=";
                        sharedFileUri+=data["token"];
                        contextNode.innerHTML="";
                        contextNode.appendChild(document
                                        .createTextNode(text));
                        let link=document.createElement("a");
                        link.setAttribute("href",sharedFileUri);
                        
                        text=document.createTextNode("Share Link");
                        link.appendChild(text);
                        contextNode.appendChild(document.createElement("br"));
                        contextNode.appendChild(link);
                        let toggle=false;
                        link.addEventListener("click",(evt)=>{
                           evt.preventDefault();    
                           if(!toggle)
                               {
                                   link.innerHTML=sharedFileUri;
                                   toggle=!toggle;
                               }
                            else
                                {
                                    link.innerHTML="Share Link";
                                    toggle=!toggle;
                                }
                        });
                    }
                else
                    {
                    
                        contextNode.innerHTML="";
                    }
                
            });
            sharePromise.catch((err)=>{
                console.log("error ",err)
            });
        });
});
}



window.onload= ()=>{
	//alert("Hi");
   
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
    });
    
};

