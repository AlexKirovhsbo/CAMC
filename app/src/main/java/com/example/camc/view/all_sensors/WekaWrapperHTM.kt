package com.example.camc.view.all_sensors


// Import necessary libraries
import weka.core.Capabilities
import weka.core.Capabilities.Capability
import weka.core.Instance
import weka.core.Instances
import weka.core.RevisionUtils
import weka.classifiers.AbstractClassifier
import java.util.logging.Logger

// Convert Java code to Kotlin
class WekaWrapper : AbstractClassifier() {
    fun globalInfo(): String {
        return toString()
    }

    override fun getCapabilities(): Capabilities {
        val result = Capabilities(this)

        result.enable(Capability.NOMINAL_ATTRIBUTES)
        result.enable(Capability.NUMERIC_ATTRIBUTES)
        result.enable(Capability.DATE_ATTRIBUTES)
        result.enable(Capability.MISSING_VALUES)
        result.enable(Capability.NOMINAL_CLASS)
        result.enable(Capability.MISSING_CLASS_VALUES)

        result.setMinimumNumberInstances(0)

        return result
    }

    override fun buildClassifier(i: Instances) {
        getCapabilities().testWithFail(i)
    }

    override fun classifyInstance(i: Instance): Double {
        val s = Array<Any?>(i.numAttributes()) { null }

        for (j in 0 until s.size) {
            if (!i.isMissing(j)) {
                if (i.attribute(j).isNominal())
                    s[j] = i.stringValue(j)
                else if (i.attribute(j).isNumeric())
                    s[j] = i.value(j)
            }
        }

        // set class value to missing
        s[i.classIndex()] = null
        return WekaClassifier.classify(s)
    }

    override fun getRevision(): String {
        return RevisionUtils.extract("1.0")
    }

    override fun toString(): String {
        return "Auto-generated classifier wrapper, based on weka.classifiers.trees.J48 (generated with Weka 3.8.6).\n" + this.javaClass.name + "/WekaClassifier"
    }

    companion object {
        val logger = Logger.getLogger(AllSensorsViewModel::class.java.name)
        fun main(args: Array<String>) {
            runClassifier(WekaWrapper(), args)
        }
    }
    class WekaClassifier {
        companion object {
            fun classify(i: Array<Any?>): Double {
                var p = Double.NaN
                p = N25d5f6b882(i)
                return p
            }

            private fun N25d5f6b882(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 1.0
                } else if ((i[1] as Double) <= 0.23) {
                    p = 1.0
                } else {
                    p = N6007773f83(i)
                }
                return p
            }

            private fun N6007773f83(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.78) {
                    p = N5a2ae59284(i)
                } else {
                    p = N51623f7f130(i)
                }
                return p
            }

            private fun N5a2ae59284(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 0.91) {
                    p = N1e59ba6185(i)
                } else {
                    p = N4cc8c77a92(i)
                }
                return p
            }

            private fun N1e59ba6185(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 0.47) {
                    p = 2.0
                } else {
                    p = N14b3ff2486(i)
                }
                return p
            }

            private fun N14b3ff2486(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 0.88) {
                    p = N4f56cc0287(i)
                } else {
                    p = 0.0
                }
                return p
            }

            private fun N4f56cc0287(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 0.65) {
                    p = N425db60f88(i)
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N425db60f88(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 2.0
                } else if ((i[2] as Double) <= 7.580469455733074) {
                    p = 2.0
                } else {
                    p = N1184869789(i)
                }
                return p
            }

            private fun N1184869789(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 0.0
                } else if ((i[2] as Double) <= 12.364131958852068) {
                    p = 0.0
                } else {
                    p = N1a45115c90(i)
                }
                return p
            }

            private fun N1a45115c90(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 0.48) {
                    p = N638ae8ca91(i)
                } else {
                    p = 0.0
                }
                return p
            }

            private fun N638ae8ca91(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 0.0
                } else if ((i[2] as Double) <= 12.40590324290193) {
                    p = 0.0
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N4cc8c77a92(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.4) {
                    p = N794d504a93(i)
                } else {
                    p = N67c9febc109(i)
                }
                return p
            }

            private fun N794d504a93(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.32) {
                    p = N4b3071ce94(i)
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N4b3071ce94(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.31) {
                    p = N7fc80cda95(i)
                } else {
                    p = N5ca78b35107(i)
                }
                return p
            }

            private fun N7fc80cda95(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 2.0
                } else if ((i[2] as Double) <= 14.467120379504701) {
                    p = 2.0
                } else {
                    p = N75b1e74e96(i)
                }
                return p
            }

            private fun N75b1e74e96(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 2.0
                } else if ((i[2] as Double) <= 19.92367589747584) {
                    p = N66ed36b297(i)
                } else {
                    p = N6bfbec8d100(i)
                }
                return p
            }

            private fun N66ed36b297(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.06) {
                    p = 2.0
                } else {
                    p = N2e9b85a098(i)
                }
                return p
            }

            private fun N2e9b85a098(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 1.09) {
                    p = N110ae27599(i)
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N110ae27599(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 1.07) {
                    p = 0.0
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N6bfbec8d100(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.04) {
                    p = N64ac0fe6101(i)
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N64ac0fe6101(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 1.27) {
                    p = N16682056102(i)
                } else {
                    p = N37f6762e106(i)
                }
                return p
            }

            private fun N16682056102(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.26) {
                    p = N50b761a4103(i)
                } else {
                    p = 0.0
                }
                return p
            }

            private fun N50b761a4103(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.13) {
                    p = N641c70bb104(i)
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N641c70bb104(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 0.0
                } else if ((i[2] as Double) <= 24.5475967430264) {
                    p = N604b90e1105(i)
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N604b90e1105(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 2.0
                } else if ((i[2] as Double) <= 21.945244408119358) {
                    p = 2.0
                } else {
                    p = 0.0
                }
                return p
            }

            private fun N37f6762e106(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 0.0
                } else if ((i[2] as Double) <= 20.532748397724287) {
                    p = 0.0
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N5ca78b35107(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 2.0
                } else if ((i[2] as Double) <= 14.764253597388004) {
                    p = N5bb9a1b2108(i)
                } else {
                    p = 0.0
                }
                return p
            }

            private fun N5bb9a1b2108(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 0.0
                } else if ((i[2] as Double) <= 6.878856099675663) {
                    p = 0.0
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N67c9febc109(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 1.44) {
                    p = N630aa36d110(i)
                } else {
                    p = N3da2c7ff113(i)
                }
                return p
            }

            private fun N630aa36d110(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.43) {
                    p = N314721a111(i)
                } else {
                    p = 0.0
                }
                return p
            }

            private fun N314721a111(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.41) {
                    p = N1536e7fb112(i)
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N1536e7fb112(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 2.0
                } else if ((i[2] as Double) <= 17.565100475977616) {
                    p = 2.0
                } else {
                    p = 0.0
                }
                return p
            }

            private fun N3da2c7ff113(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.55) {
                    p = 2.0
                } else {
                    p = Nb850aed114(i)
                }
                return p
            }

            private fun Nb850aed114(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 2.0
                } else if ((i[2] as Double) <= 6.873407978518226) {
                    p = N2b213d25115(i)
                } else {
                    p = N7a809862120(i)
                }
                return p
            }

            private fun N2b213d25115(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 2.0
                } else if ((i[2] as Double) <= 4.197515526918225) {
                    p = 2.0
                } else {
                    p = N98a1e85116(i)
                }
                return p
            }

            private fun N98a1e85116(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.66) {
                    p = 2.0
                } else {
                    p = N22bcbd74117(i)
                }
                return p
            }

            private fun N22bcbd74117(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 1.69) {
                    p = N1cd19ac118(i)
                } else {
                    p = N23a7c4a5119(i)
                }
                return p
            }

            private fun N1cd19ac118(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 1.67) {
                    p = 0.0
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N23a7c4a5119(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 0.0
                } else if ((i[2] as Double) <= 4.226575668117159) {
                    p = 0.0
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N7a809862120(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.77) {
                    p = N3d90cd5b121(i)
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N3d90cd5b121(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.74) {
                    p = N54f9a33c122(i)
                } else {
                    p = 0.0
                }
                return p
            }

            private fun N54f9a33c122(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.7) {
                    p = N49e6d7ea123(i)
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N49e6d7ea123(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.66) {
                    p = N375b153c124(i)
                } else {
                    p = N5e7d4233129(i)
                }
                return p
            }

            private fun N375b153c124(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.59) {
                    p = Nc9aec99125(i)
                } else {
                    p = N6a4592b4127(i)
                }
                return p
            }

            private fun Nc9aec99125(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.58) {
                    p = 2.0
                } else {
                    p = N15635e75126(i)
                }
                return p
            }

            private fun N15635e75126(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 2.0
                } else if ((i[2] as Double) <= 7.401055781473953) {
                    p = 2.0
                } else {
                    p = 0.0
                }
                return p
            }

            private fun N6a4592b4127(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.64) {
                    p = 2.0
                } else {
                    p = N5d88835128(i)
                }
                return p
            }

            private fun N5d88835128(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 1.65) {
                    p = 0.0
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N5e7d4233129(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 0.0
                } else if ((i[2] as Double) <= 26.035290715560638) {
                    p = 0.0
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N51623f7f130(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 1.91) {
                    p = N1774d7d5131(i)
                } else {
                    p = N621e50d4137(i)
                }
                return p
            }

            private fun N1774d7d5131(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 1.83) {
                    p = 0.0
                } else {
                    p = N56ac7c2e132(i)
                }
                return p
            }

            private fun N56ac7c2e132(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.86) {
                    p = 2.0
                } else {
                    p = N62f4d012133(i)
                }
                return p
            }

            private fun N62f4d012133(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 1.88) {
                    p = N222d92f4134(i)
                } else {
                    p = N13d6331b135(i)
                }
                return p
            }

            private fun N222d92f4134(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 1.87) {
                    p = 0.0
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N13d6331b135(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 1.89) {
                    p = N262e6043136(i)
                } else {
                    p = 0.0
                }
                return p
            }

            private fun N262e6043136(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 2.0
                } else if ((i[2] as Double) <= 6.362479035141257) {
                    p = 2.0
                } else {
                    p = 0.0
                }
                return p
            }

            private fun N621e50d4137(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 1.95) {
                    p = 0.0
                } else {
                    p = N2c6ad451138(i)
                }
                return p
            }

            private fun N2c6ad451138(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 2.91) {
                    p = Ne06642c139(i)
                } else {
                    p = 0.0
                }
                return p
            }

            private fun Ne06642c139(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 2.24) {
                    p = N5b7f21d7140(i)
                } else {
                    p = N5016438c149(i)
                }
                return p
            }

            private fun N5b7f21d7140(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 2.2) {
                    p = N4b77529c141(i)
                } else {
                    p = N403b51ee146(i)
                }
                return p
            }

            private fun N4b77529c141(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 2.15) {
                    p = N3145cfff142(i)
                } else {
                    p = 0.0
                }
                return p
            }

            private fun N3145cfff142(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 2.05) {
                    p = 0.0
                } else {
                    p = Ncdffaff143(i)
                }
                return p
            }

            private fun Ncdffaff143(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 2.09) {
                    p = 2.0
                } else {
                    p = N2e8c132a144(i)
                }
                return p
            }

            private fun N2e8c132a144(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 2.13) {
                    p = 0.0
                } else {
                    p = N5f9e47d5145(i)
                }
                return p
            }

            private fun N5f9e47d5145(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 2.0
                } else if ((i[2] as Double) <= 7.9387380800422145) {
                    p = 2.0
                } else {
                    p = 0.0
                }
                return p
            }

            private fun N403b51ee146(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 2.22) {
                    p = N33c05314147(i)
                } else {
                    p = 2.0
                }
                return p
            }

            private fun N33c05314147(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 2.22) {
                    p = 2.0
                } else {
                    p = N5016438c149(i)
                }
                return p
            }

            private fun N5016438c149(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 2.24) {
                    p = N5b7f21d7140(i)
                } else {
                    p = 0.0
                }
                return p
            }
            fun N4a38cc41150(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 2.48) {
                    p = N7bd16ee1151(i)
                } else if ((i[1] as Double) > 2.48) {
                    p = 0.0
                }
                return p
            }

            fun N7bd16ee1151(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 2.32) {
                    p = 0.0
                } else if ((i[1] as Double) > 2.32) {
                    p = N5ef749a5152(i)
                }
                return p
            }

            fun N5ef749a5152(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 2.36) {
                    p = N3c9f7bd4153(i)
                } else if ((i[1] as Double) > 2.36) {
                    p = N3049f2c2154(i)
                }
                return p
            }

            fun N3c9f7bd4153(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 2.0
                } else if ((i[2] as Double) <= 8.710491044700452) {
                    p = 2.0
                } else if ((i[2] as Double) > 8.710491044700452) {
                    p = 0.0
                }
                return p
            }

            fun N3049f2c2154(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 2.45) {
                    p = 0.0
                } else if ((i[1] as Double) > 2.45) {
                    p = N409f344c155(i)
                }
                return p
            }

            fun N409f344c155(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[2] == null) {
                    p = 2.0
                } else if ((i[2] as Double) <= 6.072546243296948) {
                    p = 2.0
                } else if ((i[2] as Double) > 6.072546243296948) {
                    p = 0.0
                }
                return p
            }

            fun N2441e79b156(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 2.0
                } else if ((i[1] as Double) <= 2.85) {
                    p = 2.0
                } else if ((i[1] as Double) > 2.85) {
                    p = N6347791c157(i)
                }
                return p
            }

            fun N6347791c157(i: Array<Any?>): Double {
                var p = Double.NaN
                if (i[1] == null) {
                    p = 0.0
                } else if ((i[1] as Double) <= 2.9) {
                    p = 0.0
                } else if ((i[1] as Double) > 2.9) {
                    p = 2.0
                }
                return p
            }
        }
    }


}

