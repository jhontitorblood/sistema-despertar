const CACHE='sistema-despertar-v18';
const CORE=['./','./index.html','./style.css','./app.js','./manifest.json','./assets/icon.svg','./assets/icon-192.png','./assets/icon-512.png','./assets/icon-sistema.png'];
self.addEventListener('install',event=>{event.waitUntil((async()=>{const c=await caches.open(CACHE);await c.addAll(CORE);await self.skipWaiting()})())});
self.addEventListener('activate',event=>{event.waitUntil((async()=>{for(const k of await caches.keys())if(k.startsWith('sistema-despertar-')&&k!==CACHE)await caches.delete(k);await self.clients.claim()})())});
self.addEventListener('fetch',event=>{if(event.request.method!=='GET'||new URL(event.request.url).origin!==location.origin)return;event.respondWith((async()=>{const c=await caches.open(CACHE);const cached=await c.match(event.request);if(cached)return cached;try{const r=await fetch(event.request);if(r.ok)c.put(event.request,r.clone());return r}catch(e){return cached||c.match('./index.html')}})())});
