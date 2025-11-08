# Protocolo Q5 — Votação "Suplemento do Mês"

## Portas e endereços
- TCP (Servidor): `localhost:7070`
- UDP Multicast (Notas informativas): `230.0.0.1:4446`

## Papéis
- **Servidor**: mantém candidatos, votos, login e prazo. Multithread (uma thread por conexão).
- **Admin**: gerencia candidatos e prazo; envia **notas** via UDP multicast.
- **User**: faz login, lista candidatos, vota e consulta resultados; recebe **notas** via UDP.

## Mensagens TCP (JSON por linha)
### Login
**Req** `{"op":"LOGIN","user":"<nome>","pass":"<senha>"}`  
**Resp** `{"ok":true,"role":"ADMIN|USER"}` ou `{"ok":false,"err":"login"}`

> Senhas padrão (ajuste em `VotingService.java`):
> - Admin: `admin` / `admin123`
> - User: qualquer usuário com senha `123`

### Listar candidatos
**Req** `{"op":"LIST"}`  
**Resp** `{"ok":true,"candidatos":[{"id":1,"nome":"WheyProtein"},{"id":2,"nome":"Creatina"},...]}`

### Votar
**Req** `{"op":"VOTE","suplementoId":<id>}`  
**Resp** `{"ok":true}` ou `{"ok":false,"err":"deadline|duplicado|not_found|login"}`

### Resultados
**Req** `{"op":"RESULTS"}`  
**Resp** `{"ok":true,"total":42,"percents":{"1":52.38,"2":47.62},"winnerId":1}`

### Administração (requer role ADMIN)
- **Abrir votação (com prazo)**  
  **Req** `{"op":"OPEN","durationSec":300}` → abre por 5 minutos  
  **Resp** `{"ok":true}`
- **Fechar**  
  **Req** `{"op":"CLOSE"}`  
  **Resp** `{"ok":true}`
- **Adicionar candidato**  
  **Req** `{"op":"ADD_CANDIDATE","nome":"PreTreino"}`  
  **Resp** `{"ok":true,"id":4}`
- **Remover candidato**  
  **Req** `{"op":"REMOVE_CANDIDATE","suplementoId":4}`  
  **Resp** `{"ok":true}`

## Mensagens UDP (Multicast)
Somente **notas informativas** (não alteram o estado do servidor):  
`{"type":"INFO","text":"Faltam 2 minutos!"}`
