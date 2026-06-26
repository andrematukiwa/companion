export function UnauthorizedScreen() {
  return (
    <div style={styles.container}>
      <div style={styles.code}>401</div>
      <div style={styles.message}>Token inválido ou expirado</div>
      <div style={styles.hint}>Gere um novo token no backend</div>
    </div>
  )
}

const styles: Record<string, React.CSSProperties> = {
  container: { display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '100%', padding: 40 },
  code: { fontSize: 72, fontWeight: 'bold', marginBottom: 16 },
  message: { fontSize: 22, marginBottom: 12 },
  hint: { fontSize: 16, color: '#555' },
}
