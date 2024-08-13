document.addEventListener('DOMContentLoaded', function () {
    // Gráfico de estatísticas do usuário
    const ctx = document.getElementById('userStats').getContext('2d');
    const userStatsChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Artigos', 'Comentários', 'Compartilhamentos'],
            datasets: [{
                label: 'Quantidade',
                data: [12, 19, 3],
                backgroundColor: ['rgba(75, 192, 192, 0.2)'],
                borderColor: ['rgba(75, 192, 192, 1)'],
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
});
