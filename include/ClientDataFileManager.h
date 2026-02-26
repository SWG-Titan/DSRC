#define SoundEvent(event, sound) CHUNK "CSND" { cstring event cstring sound }
#define ClientEffectEvent(event, effect) CHUNK "CEFT" { cstring event cstring effect }
#define WearableCustomizationSetInt(path, value) CHUNK "WCSI" { cstring path int32 value }
#define CustomizationSetInt(index, value) CHUNK "CSSI" { cstring index int32 value }
#define UseMeshGenerator(mesh) CHUNK "MESH" { cstring mesh }

// BeginWearable and EndWearable
#define BeginWearable FORM "WEAR" {
#define EndWearable }
#define NL "\n"