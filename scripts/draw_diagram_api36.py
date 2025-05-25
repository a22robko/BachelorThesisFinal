import matplotlib.pyplot as plt
import numpy as np

limit = 1000
app = "tetris"

def read_trace_file(filename):
    cpu_usage = []
    ram_usage = []
    with open(filename, 'r') as file:
        lines = file.readlines()
        for line in lines[:-1]:  # Ignore the last line (battery usage)
            cpu, ram = map(float, line.strip().split(','))
            cpu_usage.append(cpu)
            ram_usage.append(round(ram / 1024, 2))
        battery_usage = float(lines[-1].strip())  # Get the last line as battery usage
    return cpu_usage, ram_usage, battery_usage

# Read data from three trace files
cpu_flutter, ram_flutter, battery_flutter = read_trace_file(f"./data_{limit}_api36/tacelog_{app}_flutter.txt")
cpu_android, ram_android, battery_android = read_trace_file(f"./data_{limit}_api36/tacelog_{app}_android.txt")
cpu_compose, ram_compose, battery_compose = read_trace_file(f"./data_{limit}_api36/tacelog_{app}_compose.txt")

# Generate time steps (assuming one-second intervals)
time_steps = np.arange(len(cpu_flutter))

# Create figure for CPU usage plot
plt.figure(figsize=(10, 5))

# Plot CPU usage over time
plt.plot(time_steps, cpu_flutter, label='Flutter', linestyle='-', color='#1f77b4')     
plt.plot(time_steps, cpu_android, label='Kotlin + XML View', linestyle='-', color='#FECC03')  
plt.plot(time_steps, cpu_compose, label='Kotlin + Jetpack Compose', linestyle='-', color='#2ca02c')  

# Add labels and title
plt.xlim(0, limit)
plt.ylim(0)
plt.xlabel('Time (seconds)')
plt.ylabel('CPU Usage (%)')
plt.title('CPU Usage Over Time')
plt.legend()
plt.grid()

# Save CPU usage plot
plt.savefig(f"./diagrams_{limit}_api36/cpu_overtime.png", dpi=600)
plt.close()

# Create figure for RAM usage plot
plt.figure(figsize=(10, 5))

# Plot RAM usage over time
plt.plot(time_steps, ram_flutter, label='Flutter', linestyle='-', color='#1f77b4')        
plt.plot(time_steps, ram_android, label='Kotlin + XML View', linestyle='-', color='#FECC03')  
plt.plot(time_steps, ram_compose, label='Kotlin + Jetpack Compose', linestyle='-', color='#2ca02c')  

# Add labels and title
plt.xlim(0, limit)
plt.ylim(0)
plt.xlabel('Time (seconds)')
plt.ylabel('RAM Usage (MB)')
plt.title('RAM Usage Over Time')
plt.legend()
plt.grid()

# Save RAM usage plot
plt.savefig(f"./diagrams_{limit}_api36/ram_overtime.png", dpi=600)
plt.close()

# Create figure for battery consumption bar chart
plt.figure(figsize=(10, 5))

# Battery consumption data
battery_usage_values = [battery_flutter, battery_android, battery_compose]
battery_labels = ['Flutter', 'Kotlin + XML View', 'Kotlin + Jetpack Compose']

# Plot battery consumption as a bar chart
plt.bar(battery_labels, battery_usage_values, color=['#1f77b4', '#FECC03', '#2ca02c'])

# Add labels and title
plt.xlabel('Framework')
plt.ylabel('Battery Usage (mAh)')
plt.title('Battery Usage Comparison')
plt.grid(axis='y')

# Save battery consumption bar chart
plt.savefig(f"./diagrams_{limit}_api36/battery.png", dpi=600)
plt.close()

# Create figure for average CPU usage bar chart
plt.figure(figsize=(10, 5))

# Average CPU usage data
avg_cpu_usage = [np.mean(cpu_flutter), np.mean(cpu_android), np.mean(cpu_compose)]

# Plot average CPU usage as a bar chart
plt.bar(battery_labels, avg_cpu_usage, color=['#1f77b4', '#FECC03', '#2ca02c'])

# Add labels and title
plt.xlabel('Framework')
plt.ylabel('Average CPU Usage (%)')
plt.title('Average CPU Usage Comparison')
plt.grid(axis='y')

# Save average CPU usage bar chart
plt.savefig(f"./diagrams_{limit}_api36/cpu_avg.png", dpi=600)
plt.close()

# Create figure for average RAM usage bar chart
plt.figure(figsize=(10, 5))

# Average RAM usage data
avg_ram_usage = [np.mean(ram_flutter), np.mean(ram_android), np.mean(ram_compose)]

# Plot average RAM usage as a bar chart
plt.bar(battery_labels, avg_ram_usage, color=['#1f77b4', '#FECC03', '#2ca02c'])

# Add labels and title
plt.xlabel('Framework')
plt.ylabel('Average RAM Usage (MB)')
plt.title('Average RAM Usage Comparison')
plt.grid(axis='y')

# Save average RAM usage bar chart
plt.savefig(f"./diagrams_{limit}_api36/ram_avg.png", dpi=600)
plt.close()

# Create figure for CPU usage box plot
plt.figure(figsize=(10, 5))

# Prepare data for CPU box plot
cpu_data = [cpu_flutter, cpu_android, cpu_compose]
# Plot CPU usage box plot
plt.boxplot(cpu_data, labels=battery_labels, showfliers=False)
plt.ylim(0)
means = [np.mean(cpu_flutter), np.mean(cpu_android), np.mean(cpu_compose)]
plt.plot([1, 2, 3], means, 'rD', label='Mean')  # Röda diamanter
plt.legend()
plt.yticks(np.arange(0, max(means) + 30, 5))

# Add labels and title
plt.xlabel('Framework')
plt.ylabel('CPU Usage (%)')
plt.title('CPU Usage Distribution (Box Plot)')
plt.grid()

# Save CPU usage box plot
plt.savefig(f"./diagrams_{limit}_api36/cpu_box.png", dpi=600)
plt.close()

# Create figure for RAM usage box plot
plt.figure(figsize=(10, 5))

# Prepare data for RAM box plot
ram_data = [ram_flutter, ram_android, ram_compose]

# Plot RAM usage box plot
plt.boxplot(ram_data, labels=battery_labels, showfliers=False)
plt.ylim(0)

# Add labels and title
plt.xlabel('Framework')
plt.ylabel('RAM Usage (MB)')
plt.title('RAM Usage Distribution (Box Plot)')
plt.grid()

# Save RAM usage box plot
plt.savefig(f"./diagrams_{limit}_api36/ram_box.png", dpi=600)
plt.close()