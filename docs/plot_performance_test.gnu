# Set the output format and file type (e.g., PNG)
set terminal pngcairo size 800,600 enhanced font 'Helvetica,10'

# Output to a PNG file
set output 'performance_comparison.png'

# Set the title of the graph
set title "Performance Comparison: Custom vs GraphStream Dijkstra"

# Set labels for the axes
set xlabel "Nodes Count"
set ylabel "Compute Time (ms)"

# Set the range for the x-axis (optional, but helps with scaling)
set xrange [0:85000]
set yrange [0:*]

# Set grid lines for better visibility
set grid

# Define the style of the lines (for different datasets)
set style line 1 lc rgb "blue" pt 7 ps 1.5 lw 2 # Custom Dijkstra in blue
set style line 2 lc rgb "red" pt 6 ps 1.5 lw 2  # GraphStream Dijkstra in red

# Plot both Custom Dijkstra and GraphStream Dijkstra on the same graph
# The data file is assumed to be in 'performance_data.txt' with three columns:
# 1st column: Nodes Count
# 2nd column: Custom Dijkstra Compute Time (ms)
# 3rd column: GraphStream Dijkstra Compute Time (ms)

plot "performance_data.txt" using 1:2 with linespoints title "Custom Dijkstra" linestyle 1, \
     "performance_data.txt" using 1:3 with linespoints title "GraphStream Dijkstra" linestyle 2

