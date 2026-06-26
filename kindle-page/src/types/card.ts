export type CardType =
  | 'DockerCard'
  | 'CpuCard'
  | 'GitCard'
  | 'BuildCard'
  | 'SessionCard'
  | 'KanjiCard'
  | 'ArchitectureCard'
  | 'AICard'

export interface CardEvent {
  type: CardType
  title: string
  priority: number
  durationSeconds: number
  content: Record<string, unknown>
}

export type ConnectionStatus = 'connecting' | 'connected' | 'unauthorized' | 'error'
