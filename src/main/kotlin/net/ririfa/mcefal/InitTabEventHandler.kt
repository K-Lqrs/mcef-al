package net.ririfa.mcefal

import io.github.classgraph.ClassGraph
import net.ririfa.mcefal.annotations.WebifiedGUI
import net.ririfa.beacon.IEventHandler
import net.ririfa.beacon.handler
import net.ririfa.mcefal.events.ResourceManagerResolveEvent
import kotlin.reflect.full.primaryConstructor

@Suppress("unused")
class InitTabEventHandler : IEventHandler {
	override fun initHandlers() {
		handler<ResourceManagerResolveEvent> {
			MCEFAL.INSTANCE.packageName
				.flatMap { packageName ->
					ClassGraph()
						.acceptPackages(packageName)
						.enableClassInfo()
						.enableAnnotationInfo()
						.scan()
						.getClassesWithAnnotation(WebifiedGUI::class.qualifiedName)
						.mapNotNull { classInfo ->
							val clazz = classInfo.loadClass(EmbeddedBrowserScreen::class.java)
							val annotation = clazz.getAnnotation(WebifiedGUI::class.java)
							if (clazz != null && EmbeddedBrowserScreen::class.java.isAssignableFrom(clazz) && annotation.autoRegisterTabs) {
								clazz
							} else null
						}
				}
				.filterNot { it.isSynthetic }
				.forEach { screenClass ->
					runCatching {
						val instance = screenClass.kotlin.primaryConstructor?.call(screenClass.simpleName)
							?: screenClass.getDeclaredConstructor(String::class.java).newInstance(screenClass.simpleName)
						(instance as EmbeddedBrowserScreen).initTab()
					}.onFailure { e ->
						System.err.println("[ResourceManagerResolveEvent] Failed to initialize: ${screenClass.simpleName}")
						e.printStackTrace()
					}
				}
		}
	}
}