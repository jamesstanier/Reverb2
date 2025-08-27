# Reverb2 — A Novel Schroeder-Based Reverberator

Reverb2 is a digital audio **reverberation engine** inspired by the classic [Schroeder configuration](https://en.wikipedia.org/wiki/Reverberation#Digital_reverberation), but with a novel twist:  
it enhances the perceived sound quality not only through the traditional longer delay lines, but also by introducing **very short delays** that add richness, depth, and clarity without smearing the audio.  

This approach was loosely inspired by ideas discussed in [Coding a Basic Reverb Algorithm](https://medium.com/the-seekers-project/coding-a-basic-reverb-algorithm-an-introduction-to-audio-programming-d5d90ad58bde), but extends them with an original design that goes beyond conventional Schroeder reverbs.

Demo available on [YouTube](https://youtu.be/DtkGs41flzY).

---

## ✨ Key Features

- **Schroeder-inspired topology**  
  Uses a combination of parallel comb filters and serial all-pass filters.

- **Novelty: very short delay enhancement**  
  Unlike most reverbs that rely only on longer delays for spaciousness, Reverb2 leverages *short micro-delays* to improve presence and timbral detail. This makes the reverb usable as both a spatial effect *and* a subtle enhancer.

- **Configurable delay lines**  
  Tune both long (RD) and short (VD) delay values, and feedback (gain) amounts.
  
- **Automatically saves output**  
  Saves the output to an 'output.wav' file for later playback.

- **Written in Java**  
  Portable across platforms, with no native dependencies.

- **Dual-licensed**  
  Open source under **GNU GPL v3**, with a separate **commercial license** available.

---

## 🚀 Getting Started

### Requirements
- Java 17+ (tested with JDK 21, compiled to Java SE 17 compatibility)
- Maven (via the included Maven Wrapper)

### Clone and Build
```bash
git clone https://github.com/jamesstanier/Reverb2.git
cd Reverb2

# Build with Maven Wrapper (Linux/macOS)
./mvnw -q clean compile
./mvnw exec:java -Dexec.mainClass=reverb2.App

# or on Windows
.\mvnw.cmd -q clean compile
mvnw.cmd --% exec:java -Dexec.mainClass=reverb2.App
```
Or just double-click the 'Reverb2-0.0.1.jar' file (after downloading) to get it running.

---

## 📜 License

This project is **dual-licensed**:

- **GPL v3** (see [LICENSE](LICENSE))  
  You are free to use, modify, and distribute under the terms of the GNU General Public License version 3.  
- **Commercial license**  
  For closed-source or proprietary applications, a commercial license is available.  
  Please [contact me](mailto:j.stanier766@gmail.com) for details.

### Third-Party Code

This project includes code adapted from:

- [Reverberator](https://github.com/the-seekers-project/Reverberator)  
  Copyright (c) Rishi D.  
  Licensed under the [Apache License, Version 2.0](LICENSE-APACHE).

See the [NOTICE](NOTICE) file for details of modifications and attribution requirements.

---

## 🛠 Contributing

Contributions are welcome!
If you’d like to propose improvements or bug fixes, please open an issue or pull request.

## 📣 Roadmap

- Volume control
- Accept additional audio formats (other than just .wav)
- Better GUI
- VST Plugin
- Use JUCE framework