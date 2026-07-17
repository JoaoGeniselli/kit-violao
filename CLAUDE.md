# Guitar Kit (Partitura para Tablatura)

App Android com três funcionalidades: conversão de partituras para tablatura, dicionário de acordes de violão e transpositor de cifras.

## Stack

- **Kotlin 100%** (incluindo Gradle com `.kts`)
- **Jetpack Compose** com **Material Design 3**
- **Min SDK 21** / Compile SDK 33 / Target SDK 33
- JVM target: 1.8

## Estrutura de Módulos

- `app` — Aplicação principal e features
- `ui` — Biblioteca de tema compartilhado (cores, tipografia, componentes)
- `player` — Player MIDI (biblioteca)
- `buildSrc` — Configuração de build

## Arquitetura

### MVVM com Jetpack Compose

- **ViewModels** emitem estado via `StateFlow` / `Flow`, coletado na UI com `collectAsState()`
- **State classes imutáveis** como containers de estado (ex: `ChordsDictionaryState`)
- **Separação Screen/Content**: `Screen()` recebe o ViewModel, `Content()` recebe estado e callbacks puros (facilita Previews e testes)
- Features simples podem usar estado local no Composable com `mutableStateOf` sem ViewModel (ex: Score-to-Tablature)

### Injeção de Dependências — Koin

- Módulo centralizado em `app/.../di/DependencyInjection.kt`
- DSL: `single` (singletons), `factory` (instâncias por uso), `viewModel` (ViewModels)
- Integração com Compose via `koinViewModel()`

### Navegação

- **Jetpack Compose Navigation** com rotas baseadas em strings
- **Navigation Drawer** (Material3) para alternar entre features
- Enum `Feature` com `@StringRes` e rota associada

### Camada de Dados

- **Repository pattern**: interface + implementação local (ex: `ChordsRepository` → `LocalChordsRepository`)
- Dados de acordes em **CSV no assets**, parseados por `ChordParser`
- `SharedPreferences` via `SharedPreferencesClient` para configurações locais
- Sem banco de dados (Room/SQLite)

## Organização de Packages

```
com.dosei.music.scoreconverter/
├── di/                          # Módulo Koin
├── domain/                      # Modelos de domínio (Guitar, Note, OctavedNote, Tuning)
├── feature/
│   ├── home/                    # Tela inicial
│   ├── converter/tablature/     # Conversão partitura → tablatura
│   └── chords/
│       ├── dictionary/          # Dicionário de acordes
│       │   └── data/            # Repository, parser, models
│       ├── transposer/          # Transpositor de cifras
│       └── shared/              # Componentes compartilhados entre features de acordes
├── converter/                   # Conversores (MIDINoteConverter)
├── io/                          # I/O (SharedPreferencesClient)
├── main/                        # MainActivity, Feature enum, NoteMapper
├── navigation/                  # NavHost, AppDrawer
└── toolbox/                     # Utilitários (BeautifySong, CopyToClipboard, AdvertView, etc.)
```

## Convenções de Nomenclatura

| Tipo | Padrão | Exemplo |
|------|--------|---------|
| ViewModel | `[Feature]ViewModel` | `ChordsDictionaryViewModel` |
| Estado UI | `[Feature]State` | `ChordsDictionaryState` |
| Repository (interface) | `[Entity]Repository` | `ChordsRepository` |
| Repository (impl) | `Local[Entity]Repository` | `LocalChordsRepository` |
| Parser | `[Entity]Parser` | `ChordParser` |
| Tela Composable | `[Feature]Screen` | `ChordsDictionaryScreen` |
| Conteúdo Composable | `[Feature]Content` | `ChordsDictionaryContent` |
| Callable classes | `operator fun invoke()` | `CopyToClipboard`, `BeautifySong` |
| Event handlers | `on[Action]` | `onSearch`, `onTranspose` |
| Constantes | `UPPER_SNAKE_CASE` em companion object | `GROUP_DELIMITER` |

## Bibliotecas Externas Próprias

- **arpeggio** (`com.dosei.music.arpeggio`) — Renderização de diagramas de acordes e componentes de partitura/tablatura
- **ktransposer** (`com.dosei.music.ktransposer`) — Lógica de transposição musical

Ambas hospedadas no GitHub (JoaoGeniselli) e consumidas via Maven com autenticação.

## Testes

- **JUnit 4** para testes unitários
- **MockK** para mocking
- Testes focados em domain models e parsers (`MIDINoteConverterTest`, `OctavedNoteTest`, `ChordParserTest`, `GuitarStringTest`)
- `@Preview` nos Composables para validação visual
- Compose UI Test (junit4) para testes instrumentados

## Build

- **Debug**: Minificação desabilitada, IDs fake do AdMob
- **Release**: Minificação habilitada (R8), IDs reais do AdMob via `secrets.properties`
- ProGuard com `proguard-android-optimize.txt` + regras customizadas

## Regras

- UI exclusivamente em **Jetpack Compose** — não usar XML layouts
- Manter separação **Screen/Content** em Composables de feature
- Novas dependências devem ser injetadas via **Koin** no módulo centralizado
- Lógica de domínio deve ficar no package `domain/` ou nas bibliotecas externas, nunca nos Composables
- ViewModels não devem referenciar classes do Android framework (Context, etc.)
- Strings visíveis ao usuário devem ir em `strings.xml` com `@StringRes`