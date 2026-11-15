const API_BASE_URL = "/api";
async function req(path,options={}){const r=await fetch(`${API}${path}`,{headers:{'Content-Type':'application/json',...options.headers},...options});if(!r.ok)throw new Error(await r.text()||`HTTP ${r.status}`);return r.status===204?null:r.json();}
export const listArtworks=(search='',category='')=>{const p=new URLSearchParams();if(search)p.set('search',search);if(category)p.set('category',category);return req(`/api/artworks?${p}`)};
export const createArtwork=data=>req('/api/artworks',{method:'POST',body:JSON.stringify(data)});
export const updateArtwork=(id,data)=>req(`/api/artworks/${id}`,{method:'PUT',body:JSON.stringify(data)});
export const deleteArtwork=id=>req(`/api/artworks/${id}`,{method:'DELETE'});

