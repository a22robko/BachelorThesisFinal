import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import scipy.stats as stats
from statsmodels.stats.multicomp import pairwise_tukeyhsd

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
    return cpu_usage, ram_usage

# Load data from the three trace files
cpu_flutter, ram_flutter = read_trace_file(f"./data_{limit}_api36/tacelog_{app}_flutter.txt")
cpu_android, ram_android = read_trace_file(f"./data_{limit}_api36/tacelog_{app}_android.txt")
cpu_compose, ram_compose = read_trace_file(f"./data_{limit}_api36/tacelog_{app}_compose.txt")

# Combine data into single lists for ANOVA and Tukey HSD analysis
cpu_data = cpu_flutter + cpu_android + cpu_compose
ram_data = ram_flutter + ram_android + ram_compose
labels_cpu = (['Flutter'] * len(cpu_flutter) + 
              ['Kotlin + XML View'] * len(cpu_android) + 
              ['Kotlin + Jetpack Compose'] * len(cpu_compose))
labels_ram = (['Flutter'] * len(ram_flutter) + 
              ['Kotlin + XML View'] * len(ram_android) + 
              ['Kotlin + Jetpack Compose'] * len(ram_compose))

# Perform ANOVA for CPU and RAM usage
anova_cpu = stats.f_oneway(cpu_flutter, cpu_android, cpu_compose)
anova_ram = stats.f_oneway(ram_flutter, ram_android, ram_compose)

# Perform Tukey HSD test for CPU and RAM usage
tukey_cpu = pairwise_tukeyhsd(cpu_data, labels_cpu)
tukey_ram = pairwise_tukeyhsd(ram_data, labels_ram)

# Create DataFrames for results
anova_results = pd.DataFrame({
    "Metric": ["CPU Usage", "RAM Usage"],
    "F-Statistic": [f"{anova_cpu.statistic:.4f}", f"{anova_ram.statistic:.4f}"],
    "p-Value": [f"{anova_cpu.pvalue:.4f}", f"{anova_ram.pvalue:.4f}"]
})

tukey_cpu_results = pd.DataFrame(
    data=tukey_cpu.summary().data[1:],  # Skip the header row
    columns=tukey_cpu.summary().data[0]  # Use the header row as columns
).map(lambda x: f"{x:.4f}" if isinstance(x, (int, float)) else x)

tukey_ram_results = pd.DataFrame(
    data=tukey_ram.summary().data[1:],  # Skip the header row
    columns=tukey_ram.summary().data[0]  # Use the header row as columns
).map(lambda x: f"{x:.4f}" if isinstance(x, (int, float)) else x)

# Save data
anova_results.to_csv(f"diagrams_{limit}_api36/anova.txt", sep="\t", index=False)
tukey_cpu_results.to_csv(f"diagrams_{limit}_api36/tukey_cpu.txt", sep="\t", index=False)
tukey_ram_results.to_csv(f"diagrams_{limit}_api36/tukey_ram.txt", sep="\t", index=False)
