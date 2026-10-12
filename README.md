# elemental-monster-duel-api

## 📌 Overview

**elemental-monster-duel-api** is one of 6 repositories in the distributed
architecture of Elemental Monster Duel. It is the **deployable artifact** — a
Spring Boot executable jar exposing the public REST interface of Elemental
Monster Duel.

---

## 🎯 Purpose

This repository exists to:

- Expose read-only REST endpoints over the project's data
- Orchestrate persistence, conversion and transport into one running service
- Resolve locale-specific text before handing out any response
- Be the one piece of this project that's actually deployed as a service

---

## 🧱 Project Architecture Context

This repo is one of 6 in the Elemental Monster Duel ecosystem, each
independently versioned and maintained:

| Module | Responsibility | Repository |
|---|---|---|
| `db` | Database schema | https://github.com/D3vlin/elemental-monster-duel-db |
| `entity` | JPA mapping | https://github.com/D3vlin/elemental-monster-duel-entity |
| `dto` | Data transport | https://github.com/D3vlin/elemental-monster-duel-dto |
| `mapper` | Entity ↔ DTO | https://github.com/D3vlin/elemental-monster-duel-mapper |
| `api` | REST interface | — (this) |
| `ui` | Frontend | https://github.com/D3vlin/elemental-monster-duel-ui |

---

## 🚀 Why a Dedicated API Module?

- Any client only has one HTTP contract to integrate against
- The underlying libraries stay reusable instead of being locked inside one
  deployable
- Rate limiting, CORS and error handling live in exactly one place

---

## License

No license — all rights reserved.
