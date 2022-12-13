/**
 * 生成随机文件名称
 * 规则八位随机字符，加下划线连接时间戳
 */
export const buildDir = () => {
  // 以日期作为目录
  const date = new Date()
  const dir = date.getFullYear() + '/' + (date.getMonth() + 1) + '/' + date.getDate()

  function rx() {
    return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1)
  }
  return `${dir}/${date.getTime()}-${rx()}${rx()}`
}
