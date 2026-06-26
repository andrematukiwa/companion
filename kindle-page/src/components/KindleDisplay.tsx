import { CardEvent } from '../types/card'
import { ConnectionStatus } from '../types/card'
import { DockerCard } from './cards/DockerCard'
import { CpuCard } from './cards/CpuCard'
import { GitCard } from './cards/GitCard'
import { BuildCard } from './cards/BuildCard'
import { SessionCard } from './cards/SessionCard'
import { KanjiCard } from './cards/KanjiCard'
import { ArchitectureCard } from './cards/ArchitectureCard'
import { AICard } from './cards/AICard'
import { UnauthorizedScreen } from './UnauthorizedScreen'

interface Props {
  status: ConnectionStatus
  currentCard: CardEvent | null
}

export function KindleDisplay({ status, currentCard }: Props) {
  if (status === 'unauthorized') return <UnauthorizedScreen />

  return (
    <div style={styles.root}>
      <div style={styles.statusBar}>
        <span style={styles.dot(status)} />
        <span style={styles.statusText}>{statusLabel(status)}</span>
      </div>

      <div style={styles.cardArea}>
        {currentCard ? <CardRenderer card={currentCard} /> : <Waiting />}
      </div>
    </div>
  )
}

function CardRenderer({ card }: { card: CardEvent }) {
  switch (card.type) {
    case 'DockerCard':       return <DockerCard card={card} />
    case 'CpuCard':          return <CpuCard card={card} />
    case 'GitCard':          return <GitCard card={card} />
    case 'BuildCard':        return <BuildCard card={card} />
    case 'SessionCard':      return <SessionCard card={card} />
    case 'KanjiCard':        return <KanjiCard card={card} />
    case 'ArchitectureCard': return <ArchitectureCard card={card} />
    case 'AICard':           return <AICard card={card} />
    default:                 return <FallbackCard card={card} />
  }
}

function FallbackCard({ card }: { card: CardEvent }) {
  return (
    <div style={{ padding: '32px 40px' }}>
      <div style={{ fontSize: 18, fontWeight: 'bold', marginBottom: 16 }}>{card.title}</div>
      <pre style={{ fontSize: 13, whiteSpace: 'pre-wrap' }}>{JSON.stringify(card.content, null, 2)}</pre>
    </div>
  )
}

function Waiting() {
  return (
    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '100%' }}>
      <span style={{ fontSize: 16, color: '#999' }}>Aguardando cards...</span>
    </div>
  )
}

function statusLabel(status: ConnectionStatus) {
  switch (status) {
    case 'connected':   return 'conectado'
    case 'connecting':  return 'conectando...'
    case 'error':       return 'erro de conexão'
    default:            return ''
  }
}

const styles = {
  root: { display: 'flex', flexDirection: 'column', height: '100%' } as React.CSSProperties,
  statusBar: { display: 'flex', alignItems: 'center', gap: 6, padding: '8px 16px', borderBottom: '1px solid #eee' } as React.CSSProperties,
  statusText: { fontSize: 12, color: '#999', letterSpacing: 1 } as React.CSSProperties,
  cardArea: { flex: 1 } as React.CSSProperties,
  dot: (status: ConnectionStatus): React.CSSProperties => ({
    width: 8, height: 8, borderRadius: '50%',
    background: status === 'connected' ? '#000' : '#ccc',
    display: 'inline-block',
  }),
}
